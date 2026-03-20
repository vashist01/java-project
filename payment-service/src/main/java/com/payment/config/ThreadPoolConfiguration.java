package com.payment.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Queue;
import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfiguration {

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor threadPoolExecutor(MeterRegistry registry){

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(200);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        //Applies backpressure, Slows down producer instead of crashing system
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        threadPoolTaskExecutor.initialize();

        ThreadPoolExecutor threadPool = threadPoolTaskExecutor.getThreadPoolExecutor();
        // Register metrics

        registry.gauge("executor.active", threadPool, ThreadPoolExecutor::getActiveCount);
        registry.gauge("executor.queue.size", threadPool.getQueue(), Queue::size);
        registry.gauge("executor.completed", threadPool, ThreadPoolExecutor::getCompletedTaskCount);
        return threadPoolTaskExecutor;
    }

    @Bean("consumerExecutor")
    public ThreadPoolExecutor consumerExecutors(){
        int cores = Runtime.getRuntime().availableProcessors(); //Gets number of CPU cores (e.g., 8)
        return new ThreadPoolExecutor(cores*2,//8 cores → 16 threads Why?  Designed for I/O-heavy workloads
                // Threads can wait (DB/API), so we keep more threads
                cores*4, //8 cores → 32 threads 💡 Why? Handles traffic spikes Allows temporary scaling beyond core threads
                60, TimeUnit.SECONDS,//Extra threads (above core) die after 60 seconds of inactivity Saves resources during low traffic

                new ArrayBlockingQueue<>(1000), Executors.defaultThreadFactory(), //Queue holds 1000 pending tasks
                // 💡 Why important: Prevents unlimited memory usage (🔥 very important) Controls system load
                new ThreadPoolExecutor.CallerRunsPolicy()); //If:   Threads full Queue full👉 Then:  Task runs in caller thread
        // 💡 Why this is powerful:   Applies backpressure Slows down incoming requests instead of crashing system
    }
}

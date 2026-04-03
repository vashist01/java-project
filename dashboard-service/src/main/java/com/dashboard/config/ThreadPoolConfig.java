package com.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig{

    @Bean
    public Executor customExecutor(){

        return new ThreadPoolExecutor(10,50,60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(500)
        ,new ThreadPoolExecutor.CallerRunsPolicy());

    }
}

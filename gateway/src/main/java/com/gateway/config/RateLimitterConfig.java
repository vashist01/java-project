package com.gateway.config;

import com.gateway.filter.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
 public class RateLimitterConfig {
    private final JwtUtil jwtUtil;
    @Bean
    @Primary
    public RedisRateLimiter redisRateLimiter(){
        return new RedisRateLimiter(10,20);
        // replenishRate (tokens per second) //meaning 10 requests/sec normally
        // burstCapacity meaning Can spike up to 20
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);
                String userId = jwtUtil.extractUserMobileNumber(token);
                return Mono.just(userId);
            }
            return Mono.just("anonymous");
        };
    }
}

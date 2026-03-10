package com.gateway.config;

import com.gateway.enums.ServiceNameEnum;
import com.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GatewayRouterFilterConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    private final KeyResolver userKeyResolver;
    private final  RedisRateLimiter redisRateLimiter;
    @Bean
    public RouteLocator route(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes().route(ServiceNameEnum.ORDER_SERVICE.getServiceName(),
                        getOrderServiceRoutingPath("/api/order/**","lb://ORDER-SERVICE")).
        route(ServiceNameEnum.PAYMENT_SERVICE.getServiceName(),getUserServiceRoutingPath("/api/payments/**",
                "lb://PAYMENT-SERVICE")).build();

    }

    private Function<PredicateSpec, Buildable<Route>> getOrderServiceRoutingPath(String path, String url) {

        return routing -> routing.path(path).filters(gatewayFilterSpec ->
            gatewayFilterSpec.requestRateLimiter(rateLimiter ->
              rateLimiter.setRateLimiter(redisRateLimiter).
              setKeyResolver(userKeyResolver)).filter(authenticationFilter).filter((exchange, chain) -> {
                            log.info("Routing request to order-service: {}", exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }).circuitBreaker(cb -> cb
                           .setName("orderCB")
                         .setFallbackUri("forward:/orderFallback"))
                )
                .uri(url);
    }

    /*
    * What happens now at runtime
    For every request:
    Gateway receives request
    JwtAuthenticationFilter validates token
    KeyResolver extracts user
    RedisRateLimiter checks token bucket
    If allowed → forwarded
    If exceeded → Gateway returns:
    HTTP 429 TOO MANY REQUESTS
    * */
    private Function<PredicateSpec, Buildable<Route>> getUserServiceRoutingPath(String path, String url) {
        return  routing -> routing.path(path).
                filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter).
                        filter(((exchange,chain) -> {
                            log.info("Routing request to payment service: {}",
                                    exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .requestRateLimiter(rLimiter ->{
                            rLimiter.setRateLimiter(redisRateLimiter); // based on endpoint rateLimiter
                            rLimiter.setKeyResolver(userKeyResolver);
                        })
                        // THEN CIRCUIT BREAKER
                        .circuitBreaker(cb -> cb.setName("paymentCB").setFallbackUri("forward:/paymentFallback"))
                )
                .uri(url);
    }
    /*Payments:

Rate limit: 3/sec
Circuit breaker sliding window: 20
Failure threshold: 50%

User APIs:
Rate limit: 20/sec
Circuit breaker window: 50

Login:
Rate limit: 2/sec
No retry
*/
}

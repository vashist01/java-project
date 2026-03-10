package com.gateway.filter;


import com.gateway.enums.ApiVersionEnum;
import com.gateway.exception.custome.MissingRequestHeader;
import com.gateway.exception.custome.UnAuthorizeException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

    @Component
    @RequiredArgsConstructor
    public class JwtAuthenticationFilter implements GatewayFilter {

        private final static String PREFIX_API_URL = ApiVersionEnum.PREFIX_API_URL;

        private static final List<String> WHITE_LIST_URL = List.of(PREFIX_API_URL +
                ApiVersionEnum.REGISTER_USER, PREFIX_API_URL +ApiVersionEnum.LOGIN);

        private final JwtUtil jwtUtil;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Allow whitelist api url without token
            String apiPath = request.getURI().getPath();
            if(WHITE_LIST_URL.stream().anyMatch(apiPath::contains)){
                return chain.filter(exchange);
            }

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                throw new MissingRequestHeader("Missing Authorization Header", HttpStatus.UNAUTHORIZED.value());
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(!StringUtils.hasText(authHeader) || !authHeader.contains("Bearer ")){
                throw new UnAuthorizeException("Invalid Authorization Header",HttpStatus.UNAUTHORIZED.value());
            }

            String token = authHeader.substring(7);
            jwtUtil.validateToken(token);
            String mobileNumber = jwtUtil.extractUserMobileNumber(token);
            ServerHttpRequest modifiedRequest = request.mutate().header("X-USER-ID",mobileNumber).build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }
    }

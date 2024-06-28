package com.hms.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobalLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Log request details
        logger.info("Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        exchange.getRequest().getHeaders().forEach((name, values) ->
                values.forEach(value -> logger.info("Request Header: {}={}", name, value)));

        exchange.getRequest().getQueryParams().forEach((name, values) ->
                values.forEach(value -> logger.info("Request Query Param: {}={}", name, value)));

        // Continue the filter chain and log response details
        return chain.filter(exchange).doOnSuccess(aVoid -> {
            HttpStatus statusCode = (HttpStatus) exchange.getResponse().getStatusCode();
            if (statusCode != null) {
                logger.info("Response Status: {}", statusCode);
                exchange.getResponse().getHeaders().forEach((name, values) ->
                        values.forEach(value -> logger.info("Response Header: {}={}", name, value)));
            }
        }).doOnError(throwable -> {
            logger.error("Error occurred while processing request: {}", throwable.getMessage(), throwable);
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

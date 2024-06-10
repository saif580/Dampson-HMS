package com.hms.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGatewayFilterFactory.class);

    public LoggingGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            logger.info("Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Response Status: {}", exchange.getResponse().getStatusCode());
            }));
        };
    }

    public static class Config {
        // Put the configuration properties for your filter here
    }
}

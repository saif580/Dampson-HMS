package com.hms.usersmicroservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.logging.Logger;

@Configuration
public class FeignClientConfig {

    private static final Logger logger = Logger.getLogger(FeignClientConfig.class.getName());

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getCredentials() != null) {
                    String token = authentication.getCredentials().toString();
                    requestTemplate.header("Authorization", "Bearer " + token);
                    logger.info("Added Authorization header to Feign request: Bearer " + token);
                } else {
                    logger.warning("Authentication or token is null.");
                }
            }
        };
    }
}

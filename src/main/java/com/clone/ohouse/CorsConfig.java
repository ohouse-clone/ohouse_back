package com.clone.ohouse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    public static final String ALLOW_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,OPTION";
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(ALLOW_METHOD_NAMES.split(","));

    }
}

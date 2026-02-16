package com.example.hmrback.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() throws IOException {
        ClassPathResource resource = new ClassPathResource("openapi/openapi.yaml");
        String yaml = new String(resource.getInputStream()
                                         .readAllBytes());
        return io.swagger.v3.core.util.Yaml.mapper()
                                           .readValue(yaml,
                                                      OpenAPI.class);
    }


}


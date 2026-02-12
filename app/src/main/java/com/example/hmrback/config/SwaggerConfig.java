package com.example.hmrback.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth",
                                                                bearerAuth))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }


    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        ClassPathResource resource = new ClassPathResource("openapi/openapi.yaml");
        String yaml = new String(resource.getInputStream()
                                         .readAllBytes());
        return io.swagger.v3.core.util.Yaml.mapper()
                                           .readValue(yaml,
                                                      OpenAPI.class);
    }


}


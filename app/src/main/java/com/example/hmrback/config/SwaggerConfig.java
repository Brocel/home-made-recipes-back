package com.example.hmrback.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        SecurityScheme oidc = new SecurityScheme()
            .type(SecurityScheme.Type.OPENIDCONNECT)
            .openIdConnectUrl("https://accounts.google.com/.well-known/openid-configuration");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("google-oidc", oidc))
            .addSecurityItem(new SecurityRequirement().addList("google-oidc"));
    }

}

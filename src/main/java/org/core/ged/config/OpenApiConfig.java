package org.core.ged.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("GED API")
                .version("0.0.1")
                .description("API for the GED application"));
    }
}

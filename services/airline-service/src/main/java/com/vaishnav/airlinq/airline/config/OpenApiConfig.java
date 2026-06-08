package com.vaishnav.airlinq.airline.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI locationServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Airline Service")
                        .description("This is the documentation of airline service API")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.3.2"))
                )
                .externalDocs(new ExternalDocumentation()
                        .url("https://github.com/vaishnav/airline-service")
                        .description("More information about the airline service")
                );
    }
}


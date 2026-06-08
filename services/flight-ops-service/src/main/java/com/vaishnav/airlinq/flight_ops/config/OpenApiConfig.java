package com.vaishnav.airlinq.flight_ops.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI flightOpsServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Flight OPS Service")
                        .description("This is the documentation of Flight OPS service API")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.3.2"))
                )
                .externalDocs(new ExternalDocumentation()
                        .url("https://github.com/vaishnav/Flight-OPS-service")
                        .description("More information about the Flight OPS service")
                );
    }
}


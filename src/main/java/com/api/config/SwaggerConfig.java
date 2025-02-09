package com.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Monitoring documentation",
                description = "Monitoring app with crud for obtain plants and her sensors with authentication for users"
        )
)
public class SwaggerConfig {

}

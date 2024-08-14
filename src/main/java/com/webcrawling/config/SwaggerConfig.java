package com.webcrawling.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info().title("BootAgit API Title")
                        .description("BootAgit API Description")
                        .version("1.0"));
    }

}

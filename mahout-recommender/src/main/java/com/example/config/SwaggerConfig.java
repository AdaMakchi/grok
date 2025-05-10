package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI mahoutRecommenderAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mahout Recommender API")
                        .description("API pour le système de recommandation basé sur Apache Mahout")
                        .version("1.0"));
    }
}
package com.meraj.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI secureOpenAPI() {
		return new OpenAPI()		
				// 🔒 Add JWT Bearer Authentication
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                // 📝 API Info section
                .info(new Info()
                        .title("Blog API")
                        .description("A Spring Boot REST API for managing posts, comments, and likes — secured with JWT.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Meraj Ahamed")
                                .email("meraj@example.com")
                                .url("https://github.com/merajahamed"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
	}
}

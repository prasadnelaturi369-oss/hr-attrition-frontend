package com.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	private static final String API_KEY = "X-API-KEY";

	@Bean
	public OpenAPI customOpenAPI() {

		return new OpenAPI()
				.info(new Info().title("API Gateway Simulation System").version("1.0")
						.description("API Gateway with POST Support"))

				.components(new Components().addSecuritySchemes(API_KEY,
						new SecurityScheme().name("X-API-KEY").type(SecurityScheme.Type.APIKEY)
								.in(SecurityScheme.In.HEADER)))

				.security(List.of(new SecurityRequirement().addList(API_KEY)));
	}
}
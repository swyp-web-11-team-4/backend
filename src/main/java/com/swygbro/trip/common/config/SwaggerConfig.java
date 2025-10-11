package com.swygbro.trip.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER,
    paramName = "Authorization")
public class SwaggerConfig {
  @Value("${swagger.url:http://localhost:8080}")
  private String url;

  @Bean
  public OpenAPI openAPI() {
    Contact contact =
        new Contact().name("SWYP Web 11기 Team 4").url("https://github.com/swyp-web-11-team-4");

    Info info =
        new Info()
            .title("API Documentation")
            .version("0.0.1")
            .description("SWYP Web 11기 Team 4 Backend API Docs")
            .contact(contact);

    Server server = new Server().url(url);

    SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

    return new OpenAPI().info(info).servers(List.of(server)).addSecurityItem(securityRequirement);
  }
}

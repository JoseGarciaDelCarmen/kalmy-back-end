package com.base.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Configuration for the Swagger API documentation.
 */
@Configuration
public class SwaggerConfig {
  /**
   * The URL of the local development server for the OpenAPI documentation.
   */
  @Value("${openapi.local-url}")
  private String localDevUrl;

  /**
   * The URL of the development server for the OpenAPI documentation.
   */
  @Value("${openapi.dev-url}")
  private String devUrl;

  /**
   * The environment context of the application, containing profile and property information.
   * It is used to determine the active profiles and configure the OpenAPI documentation accordingly.
   */
  @Autowired
  private Environment environment;

  /**
   * Creates the OpenAPI configuration with custom information and server details.
   * It dynamically adds server URLs based on the active Spring profiles, allowing the documentation to reflect the
   * correct server endpoints for different deployment environments.
   * This method sets up basic API information and contact details, and then adds server information specific to the
   * active environment (like development or local).
   *
   * @return An instance of {@link OpenAPI} with configured metadata and server information.
   */
  @Bean
  public OpenAPI openAPI() {
    OpenAPI openAPI = new OpenAPI().info(new Info().title("Demo API").version("1.0").contact(
                                                       new Contact().name("Support Team").email("support@example.com"))
                                                   .description("API for managing Demo endpoints."))
                                   .addSecurityItem(new SecurityRequirement().addList("bearerAuth")).components(
            new Components().addSecuritySchemes("bearerAuth",
                                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                                                                    .bearerFormat("JWT")));

    if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
      openAPI.addServersItem(new Server().url(devUrl).description("Server URL in Development environment"));
    }

    if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
      openAPI.addServersItem(new Server().url(localDevUrl).description("Server URL in Local environment"));
    }

    // Add more environments as needed

    return openAPI;
  }
}


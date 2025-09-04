package com.base.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing (CORS) in the application.
 * <p>
 * This configuration ensures that requests from allowed origins can access the API endpoints.
 * Settings are externalized via {@code application.yml}, enabling flexible configuration
 * for different environments (dev, staging, prod).
 * <p>
 * CORS details:
 * <ul>
 *   <li>Allowed origins are defined in {@code app.cors.allowed-origins}.</li>
 *   <li>Allowed HTTP methods include GET, POST, PUT, PATCH, DELETE, OPTIONS.</li>
 *   <li>All headers are allowed.</li>
 *   <li>Exposed headers include {@code x-auth-token} and {@code x-app-id}.</li>
 *   <li>Credentials (cookies/authorization headers) are permitted.</li>
 *   <li>Preflight response cache duration is {@link #MAX_AGE} seconds.</li>
 * </ul>
 *
 * @see WebMvcConfigurer
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  /**
   * Array of allowed origins. Values are injected from the application configuration file.
   * Example in {@code application.yml}:
   * <pre>
   * app:
   *   cors:
   *     allowed-origins: "http://localhost:4200,https://miapp.web.app"
   * </pre>
   */
  @Value("${app.cors.allowed-origins}")
  private String[] allowedOrigins;

  /**
   * The maximum age (in seconds) for caching preflight responses.
   * Default: 3600 seconds (1 hour).
   */
  private static final int MAX_AGE = 3600;

  /**
   * Configures the CORS mapping for the application.
   * <p>
   * Applies to all endpoints with the defined allowed origins, methods, headers,
   * exposed headers, and credentials policy.
   *
   * @param registry the CORS registry to configure
   */
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("x-auth-token", "x-app-id")
            .allowCredentials(true)
            .maxAge(MAX_AGE);
  }
}

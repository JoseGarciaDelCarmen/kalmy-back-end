package com.base.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuration for the API.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

  /**
   * Adds resource handlers for serving static resources such as 'swagger-ui.html' and webjars.
   * This method configures the locations from which to serve these resources.
   *
   * @param registry the resource handler registry used to configure the resource handlers
   */
  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  /**
   * Creates a new {@link RestTemplate} instance
   * using the given {@link RestTemplateBuilder}.
   *
   * @param builder the RestTemplateBuilder used
   *                to create the RestTemplate instance
   *
   * @return a new RestTemplate instance
   */
  @Bean
  @ConditionalOnMissingBean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }

  /**
   * Creates a new {@link ObjectMapper} instance for JSON processing.
   *
   * @return a new ObjectMapper instance
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }
}

package com.base.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")        // solo cuando corras con el perfil 'dev'
public class NoSecurityConfig {

  @Bean
  @Order(0) // mayor prioridad que otras cadenas
  SecurityFilterChain noAuth(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .httpBasic(b -> b.disable())
        .formLogin(f -> f.disable());
    return http.build();
  }
}

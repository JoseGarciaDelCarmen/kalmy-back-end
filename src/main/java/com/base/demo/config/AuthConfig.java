package com.base.demo.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

  @Bean
  GoogleIdTokenVerifier googleIdTokenVerifier(@Value("${google.client-id}") String clientId)
      throws GeneralSecurityException, IOException {
    return new GoogleIdTokenVerifier.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance())
        .setAudience(Collections.singletonList(clientId))
        .setIssuer("https://accounts.google.com")
        .build();
  }
}

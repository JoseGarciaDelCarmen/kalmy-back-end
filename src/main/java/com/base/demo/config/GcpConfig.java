package com.base.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStream;

@Configuration
public class GcpConfig {

  @Value("${app.gcp.project-id}")
  private String projectId;

  @Value("${app.gcp.credentials.location:}") // ej: file:C:/keys/sa.json o classpath:sa.json
  private Resource credentialsLocation;

  @Bean
  GoogleCredentials googleCredentials() throws Exception {
    try (InputStream in = credentialsLocation.getInputStream()) {
      return GoogleCredentials.fromStream(in);
    }
  }

  @Bean
  Firestore firestore(GoogleCredentials creds) {
    return FirestoreOptions.newBuilder()
                           .setProjectId(projectId)
                           .setCredentials(creds)
                           .build()
                           .getService();
  }

  @Bean
  Storage storage(GoogleCredentials creds) {
    return StorageOptions.newBuilder()
                         .setProjectId(projectId)
                         .setCredentials(creds)
                         .build()
                         .getService();
  }
}

package com.base.demo.model.entity;

import java.time.Instant;

public class Customer {
  private String id;
  private String name;
  private String email;
  private Instant createdAt;

  public Customer() {} // requerido por Firestore para deserializar

  public Customer(String id, String name, String email, Instant createdAt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}

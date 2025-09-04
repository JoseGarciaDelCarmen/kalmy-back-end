package com.base.demo.repository;

import com.base.demo.model.DTO.customer.CustomerResponse;
import com.base.demo.model.entity.Customer;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {
  private static final String COLLECTION = "customers";
  private final Firestore db;

  public String save(Customer c) throws Exception {
    String id = (c.getId() != null) ? c.getId() : UUID.randomUUID().toString();

    Map<String, Object> doc = Map.of(
        "name", c.getName(),
        "email", c.getEmail(),
        "createdAt", com.google.cloud.firestore.FieldValue.serverTimestamp()
    );

    db.collection(COLLECTION).document(id).set(doc).get();
    return id;
  }

  public Optional<CustomerResponse> findById(String id) throws Exception {
    var snap = db.collection(COLLECTION).document(id).get().get();
    if (!snap.exists()) return Optional.empty();

    var ts = snap.getTimestamp("createdAt");
    Instant created = (ts != null) ? Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos()) : null;

    var c = new CustomerResponse(
        snap.getId(),
        snap.getString("name"),
        snap.getString("email"),
        created
    );
    return Optional.of(c);
  }
}

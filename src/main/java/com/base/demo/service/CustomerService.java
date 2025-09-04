package com.base.demo.service;

import com.base.demo.exception.BadRequestException;
import com.base.demo.exception.DataStoreException;
import com.base.demo.exception.NotFoundException;
import com.base.demo.model.DTO.customer.CreateCustomerRequest;
import com.base.demo.model.DTO.customer.CustomerResponse;
import com.base.demo.model.entity.Customer;
import com.base.demo.repository.CustomerRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final Firestore db;
  private final CustomerRepository repo;

  public String create(CreateCustomerRequest req) throws Exception {
    var entity = new Customer(null, req.name(), req.email(), null);
    return repo.save(entity);
  }

  public CustomerResponse get(String id) {
    if (id == null || id.isBlank()) {
      throw new BadRequestException("id must not be blank");
    }

    try {
      DocumentSnapshot snap = db.collection("customers").document(id).get().get();
      if (!snap.exists()) {
        throw new NotFoundException("Customer %s not found".formatted(id));
      }
      return map(snap);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DataStoreException("Interrupted while calling Firestore", e);
    } catch (ExecutionException e) {
      throw new DataStoreException("Firestore error", e.getCause());
    }
  }


  public List<CustomerResponse> list(int size) throws Exception {
    return db.collection("customers")
             .orderBy("createdAt", com.google.cloud.firestore.Query.Direction.DESCENDING)
             .limit(size)
             .get().get()
             .getDocuments().stream().map(this::map).toList();
  }

  private CustomerResponse map(DocumentSnapshot d) {
    var ts = d.getTimestamp("createdAt");
    Instant created = ts != null ? Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos()) : null;
    return new CustomerResponse(d.getId(), d.getString("name"), d.getString("email"), created);
  }
}

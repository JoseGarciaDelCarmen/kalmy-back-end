package com.base.demo.controller;

import com.base.demo.model.CustomApiResponse;
import com.base.demo.model.DTO.customer.CreateCustomerRequest;
import com.base.demo.model.DTO.customer.CustomerResponse;
import com.base.demo.service.CustomerService;
import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerService service;

  @GetMapping("/{id}")
  public ResponseEntity<CustomApiResponse<CustomerResponse>> get(@PathVariable String id) {
    CustomerResponse customer = service.get(id);
    return ResponseEntity.ok(new CustomApiResponse<>("Customer fetched successfully", customer));
  }


  @GetMapping
  public CustomApiResponse<List<CustomerResponse>> list(@RequestParam(defaultValue = "20") int size) throws Exception {
    return new CustomApiResponse<>("ok", service.list(size));
  }

  @PostMapping
  public ResponseEntity<CustomApiResponse<String>> create(@Valid @RequestBody CreateCustomerRequest req) throws Exception {
    String id = service.create(req);
    return ResponseEntity.ok(new CustomApiResponse<>("created", id));
  }
}

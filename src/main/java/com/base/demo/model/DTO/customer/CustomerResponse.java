package com.base.demo.model.DTO.customer;

import java.time.Instant;

public record CustomerResponse(
    String id,
    String name,
    String email,
    Instant createdAt
) {}
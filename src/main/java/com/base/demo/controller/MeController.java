package com.base.demo.controller;

import com.base.demo.model.CustomApiResponse;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

  @GetMapping("/me")
  public CustomApiResponse<Map<String, Object>> me(Authentication auth) {
    var details = auth != null ? auth.getDetails() : null; // claims del token
    assert details != null;
    return new CustomApiResponse<>("ok", Map.of(
        "principal", auth.getName(),
        "claims", details
    ));
  }
}

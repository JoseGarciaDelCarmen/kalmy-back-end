package com.base.demo.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GisIdTokenVerifier {

  private final GoogleIdTokenVerifier verifier;

  public GisIdTokenVerifier(GoogleIdTokenVerifier verifier) {
    this.verifier = verifier;
  }

  public Optional<UserInfo> verify(String idToken) {
    try {
      GoogleIdToken token = verifier.verify(idToken);
      if (token == null) return Optional.empty();
      Payload p = token.getPayload();
      return Optional.of(new UserInfo(
          (String) p.get("sub"),
          (String) p.get("email"),
          (String) p.get("name"),
          (String) p.get("picture"),
          p));
    } catch (GeneralSecurityException | IOException e) {
      return Optional.empty();
    }
  }

  public record UserInfo(String sub, String email, String name, String picture, Map<String, Object> claims) {}
}

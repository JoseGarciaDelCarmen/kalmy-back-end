package com.base.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class BearerTokenAuthFilter extends OncePerRequestFilter {

  private final GisIdTokenVerifier verifier;

  public BearerTokenAuthFilter(GisIdTokenVerifier verifier) {
    this.verifier = verifier;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String header = req.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      Optional<GisIdTokenVerifier.UserInfo> user = verifier.verify(token);
      if (user.isPresent()) {
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var auth = new UsernamePasswordAuthenticationToken(user.get().email(), null, authorities);
        auth.setDetails(user.get().claims()); // podrás leer claims en /me
        SecurityContextHolder.getContext().setAuthentication(auth);
      } else {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid ID token");
        return;
      }
    }
    chain.doFilter(req, res);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String uri = request.getRequestURI();
    return uri.startsWith("/public/")
        || uri.equals("/actuator/health")
        || uri.startsWith("/v3/api-docs")
        || uri.startsWith("/swagger-ui");
  }
}

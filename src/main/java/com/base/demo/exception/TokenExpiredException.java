package com.base.demo.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when a JWT token is expired.
 *
 * <p>This custom exception is thrown during the authentication process when the system detects that a provided JWT
 * token has passed its expiration time and is no longer valid. It extends
 * {@link AuthenticationException}, which is a standard exception used by Spring Security for authentication failures
 * .</p>
 */
public class TokenExpiredException extends AuthenticationException {
  /**
   * Constructs a new {@code TokenExpiredException} with the specified detail message.
   *
   * @param message the detail message that describes the reason for the exception
   */
  public TokenExpiredException(String message) {
    super(message);
  }
}

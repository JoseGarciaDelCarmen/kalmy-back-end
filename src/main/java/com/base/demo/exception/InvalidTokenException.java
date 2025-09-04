package com.base.demo.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when a JWT token is invalid.
 *
 * <p>This custom exception is thrown during the authentication process when the system detects that a provided JWT
 * token is either malformed, has missing claims, or cannot be verified. It extends {@link AuthenticationException},
 * which is a standard exception used by Spring Security for authentication failures.</p>
 */
public class InvalidTokenException extends AuthenticationException {
  /**
   * Constructs a new {@code InvalidTokenException} with the specified detail message.
   *
   * @param message the detail message that describes the reason for the exception
   */
  public InvalidTokenException(String message) {
    super(message);
  }
}
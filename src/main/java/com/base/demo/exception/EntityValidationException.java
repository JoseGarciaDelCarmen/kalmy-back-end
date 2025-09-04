package com.base.demo.exception;

/**
 * Exception thrown when validation on an entity fails. This class is used across the application to signal
 * validation errors that occur during the creation or update of entities.
 *
 * <p>
 * This exception is often thrown in service where input data for entities are validated against specific business
 * rules or constraints. It extends {@link RuntimeException} to allow for unchecked exception handling, facilitating
 * a cleaner codebase by not requiring methods to explicitly catch or declare it, thus allowing higher-level handlers
 * to manage the exception.
 * </p>
 *
 * <p>
 * The message associated with the exception should provide a detailed explanation of the validation failure to aid
 * in debugging and user feedback.
 * </p>
 */
public class EntityValidationException extends RuntimeException {

  /**
   * Constructs a new {@code EntityValidationException} with the specified detail message. The detail message is
   * saved for later retrieval by the {@link Throwable#getMessage()} method.
   *
   * @param message the detail message. The detail message is intended to provide a clear and concise description of
   *                the validation failure, including, if applicable, the specific reasons why the validation did not
   *                pass, to assist in debugging and to  inform the user.
   */
  public EntityValidationException(final String message) {
    super(message);
  }

  /**
   * Constructs a new {@code EntityValidationException} with the specified detail message and cause.
   *
   * @param message the detail message.
   * @param cause   the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).
   */
  public EntityValidationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}

package com.base.demo.exception;

/**
 * Exception thrown when a conflict occurs during the operation on an entity, indicating that the requested operation
 * cannot be completed due to a conflict with the current state of the target resource.
 * <p>
 * This exception is typically thrown in scenarios such as attempting to create or update an entity with a unique
 * constraint that is already present in the database, indicating a violation of business logic.
 * </p>
 */
public class EntityConflictException extends RuntimeException {
  /**
   * Constructs a new {@code EntityConflictException} with the specified detail message.
   * The message can be used to provide additional information about the conflict and suggest further actions to
   * resolve it.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link Throwable#getMessage()} method.
   */
  public EntityConflictException(final String message) {
    super(message);
  }
}

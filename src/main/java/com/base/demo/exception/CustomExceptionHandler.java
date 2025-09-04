package com.base.demo.exception;

import com.base.demo.model.CustomApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Global exception handler class that captures and handles custom exceptions thrown by the application.
 * This class ensures that all exceptions are caught and handled gracefully, returning a standardized response to the
 * client regardless of the error occurred.
 */
@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<CustomApiResponse<Void>> badRequest(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new CustomApiResponse<>(ex.getMessage(), null));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<CustomApiResponse<Void>> notFound(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                         .body(new CustomApiResponse<>(ex.getMessage(), null));
  }

  @ExceptionHandler(DataStoreException.class)
  public ResponseEntity<CustomApiResponse<Void>> datastore(DataStoreException ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                         .body(new CustomApiResponse<>("Upstream data store error: " + ex.getMessage(), null));
  }



  /**
   * Handles {@link EntityValidationException} by returning an {@link CustomApiResponse} with a BAD REQUEST status
   * and a message describing the validation error.
   *
   * @param ex The caught {@link EntityValidationException}.
   *
   * @return A {@link ResponseEntity} containing an {@link CustomApiResponse} with error details and HTTP status code.
   */
  @ExceptionHandler(EntityValidationException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleEntityValidationException(final EntityValidationException ex) {
    CustomApiResponse<Object> customApiResponse = new CustomApiResponse<>(ex.getMessage(), null);
    return new ResponseEntity<>(customApiResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link EntityConflictException} by returning an {@link CustomApiResponse} with a CONFLICT status and a
   * message describing the conflict.
   *
   * @param ex The caught {@link EntityConflictException}.
   *
   * @return A {@link ResponseEntity} containing an {@link CustomApiResponse} with error details and HTTP status code.
   */
  @ExceptionHandler(EntityConflictException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleEntityConflict(final EntityConflictException ex) {
    CustomApiResponse<Object> customApiResponse = new CustomApiResponse<>(ex.getMessage(), null);
    return new ResponseEntity<>(customApiResponse, HttpStatus.CONFLICT);
  }

  /**
   * Handles {@link MethodArgumentTypeMismatchException} when a request parameter cannot be converted to its declared
   * type.
   * This typically occurs when a client provides an invalid format for a path variable or query parameter that is
   * expected to be of a specific type, such as {@code Long}, but receives a non-convertible value, like a {@code
   * String} that cannot be parsed to a {@code Long}.
   *
   * @param ex The exception instance containing information about the mismatch, including the expected type and the
   *           provided value.
   *
   * @return A {@link ResponseEntity} object containing a {@link CustomApiResponse} which includes a message
   * detailing the error, and sets the HTTP status to {@link HttpStatus#BAD_REQUEST} to indicate that the client
   * request was malformed or incorrect.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleMethodArgumentTypeMismatch(
      final MethodArgumentTypeMismatchException ex) {
    String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(),
                                   ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
    CustomApiResponse<Object> response = new CustomApiResponse<>(message, null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles exceptions when the validation on an argument annotated with {@code @Valid} fails.
   * Collects all the field errors from the {@link MethodArgumentNotValidException} and constructs a map of field
   * names to error messages. This provides a detailed account of all validation failures to the client.
   *
   * @param ex The exception that captures the details of the validation errors.
   *
   * @return A {@link ResponseEntity} containing a {@link CustomApiResponse} with detailed validation error messages
   * and a BAD REQUEST status code.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    CustomApiResponse<Object> response = new CustomApiResponse<>("Validation error", errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles access denied exceptions that occur when a request is made by a client without sufficient permissions.
   * This is typically thrown by
   * Spring Security when an authenticated user attempts to access a resource they do not have permission for.
   *
   * @param ex The exception that indicates the access was denied.
   *
   * @return A {@link ResponseEntity} containing a {@link CustomApiResponse} with an "Access denied" message and a
   * FORBIDDEN status code.
   */
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<CustomApiResponse<Object>> handleAccessDeniedException(final AccessDeniedException ex) {
    CustomApiResponse<Object> response = new CustomApiResponse<>("Access denied from Security config", null);
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  /**
   * Handles exceptions that occur when the request body is malformed and cannot be read or understood by the server.
   * This can happen if the JSON is badly formatted, missing required fields, or otherwise corrupt.
   *
   * @param ex The exception that captures the details of why the request body could not be parsed.
   *
   * @return A {@link ResponseEntity} containing a {@link CustomApiResponse} with a "Malformed request body" message
   * and a BAD REQUEST status code.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleHttpMessageNotReadable(
      final HttpMessageNotReadableException ex) {
    CustomApiResponse<Object> response = new CustomApiResponse<>("Malformed request body", null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles exceptions when an HTTP request is made using a method not supported by the endpoint.
   * For example, if an endpoint only supports POST requests but a GET request is made instead.
   *
   * @param ex The exception that captures the details of the unsupported HTTP method.
   *
   * @return A {@link ResponseEntity} containing a {@link CustomApiResponse} with a "Method not allowed" message and
   * a METHOD NOT ALLOWED status code.
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleHttpRequestMethodNotSupported(
      final HttpRequestMethodNotSupportedException ex) {
    CustomApiResponse<Object> response = new CustomApiResponse<>("Method not allowed", null);
    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * Handles exceptions when the media type of the request is not supported.
   * This may occur when the client sends a request with a content type
   * that the server cannot handle.
   *
   * @param ex The captured unsupported media type exception.
   *
   * @return A response entity containing an error message and the HTTP UNSUPPORTED_MEDIA_TYPE status code.
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleHttpMediaTypeNotSupportedException(
      final HttpMediaTypeNotSupportedException ex) {
    CustomApiResponse<Object> response = new CustomApiResponse<>("Media type not supported", null);
    return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /**
   * Handles exceptions when the media type requested in the 'Accept' header is not acceptable.
   *
   * @param ex The captured not acceptable media type exception.
   *
   * @return A response entity containing an error message and the HTTP NOT_ACCEPTABLE status code.
   */
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleHttpMediaTypeNotAcceptableException(
      final HttpMediaTypeNotAcceptableException ex) {
    CustomApiResponse<Object> response = new CustomApiResponse<>("Media type not acceptable", null);
    return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * Handles the timeout of asynchronous requests when a request does not complete within the expected time.
   *
   * @param ex The captured asynchronous request timeout exception.
   *
   * @return A response entity containing an error message and the HTTP SERVICE_UNAVAILABLE status code.
   */
  @ExceptionHandler(AsyncRequestTimeoutException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleAsyncRequestTimeoutException(
      final AsyncRequestTimeoutException ex) {
    CustomApiResponse<Object> response = new CustomApiResponse<>("Async request timeout", null);
    return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
  }

  /**
   * Handles token expiration errors by returning an {@link CustomApiResponse} with an UNAUTHORIZED status
   * and a message indicating that the token is expired.
   *
   * @param ex The caught {@link TokenExpiredException}.
   * @return A {@link ResponseEntity} containing an {@link CustomApiResponse} with error details and HTTP status code.
   */
  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleTokenExpiredException(final TokenExpiredException ex) {
    CustomApiResponse<Object> customApiResponse = new CustomApiResponse<>("Token expired: " + ex.getMessage(), null);
    return new ResponseEntity<>(customApiResponse, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles invalid token errors by returning an {@link CustomApiResponse} with an UNAUTHORIZED status
   * and a message indicating that the token is invalid.
   *
   * @param ex The caught {@link InvalidTokenException}.
   * @return A {@link ResponseEntity} containing an {@link CustomApiResponse} with error details and HTTP status code.
   */
  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleInvalidTokenException(final InvalidTokenException ex) {
    CustomApiResponse<Object> customApiResponse = new CustomApiResponse<>("Invalid token: " + ex.getMessage(), null);
    return new ResponseEntity<>(customApiResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(com.google.cloud.firestore.FirestoreException.class)
  public ResponseEntity<CustomApiResponse<Object>> handleFirestore(com.google.cloud.firestore.FirestoreException ex) {
    var body = new CustomApiResponse<>("Firestore error: " + ex.getMessage(), null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler({ java.util.concurrent.ExecutionException.class, java.lang.InterruptedException.class })
  public ResponseEntity<CustomApiResponse<Object>> handleAsync(Throwable ex) {
    var body = new CustomApiResponse<>("Async error: " + ex.getMessage(), null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  /**
   * Handles general exceptions by returning an {@link CustomApiResponse} with an INTERNAL SERVER ERROR status and a
   * generic error message.
   * This method catches all other exceptions that are not explicitly handled by other @ExceptionHandler methods.
   *
   * @param ex The caught {@link Exception}.
   *
   * @return A {@link ResponseEntity} containing an {@link CustomApiResponse} with error details and HTTP status code.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomApiResponse<Object>> handleGeneralException(final Exception ex) {
    var customApiResponse = new CustomApiResponse<>("An unexpected error occurred.", null);
    return new ResponseEntity<>(customApiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

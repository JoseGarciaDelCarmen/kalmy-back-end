package com.base.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Provides a standard format for API responses, encapsulating both a message and an optional data payload.
 * This class is designed to be used as a generic response wrapper, where {@code T} represents the type of the data
 * payload.
 *
 * @param <T> the type of the data payload in the response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Provides a standard format for API responses, encapsulating both a message and an optional " +
    "data payload.")
public class CustomApiResponse<T> {
  /**
   * Additional information about the API response, helping to clarify the outcome or state returned.
   * This message can be used for debugging, user feedback, or as a simple status message.
   */
  @Schema(description = "Additional information about the API response, helping to clarify the outcome or state " +
      "returned.")
  private String message;

  /**
   * The actual data payload of the response. This field may be null if the response does not return any data.
   * It is generic, allowing for flexibility in the type of data returned by different methods.
   */
  @Schema(description = "The actual data payload of the response. This field may be null if the response does not " +
      "return any data.")
  private T data;
}

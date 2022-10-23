package rocks.danielw.web.api;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

  private final LocalDateTime timestamp;
  private List<String> messages = new ArrayList<>();

  public ErrorResponse(String message) {
    this.timestamp = LocalDateTime.now();
    messages.add(message);
  }

  public ErrorResponse(List<String> messages) {
    this.timestamp = LocalDateTime.now();
    this.messages  = messages;
  }
}

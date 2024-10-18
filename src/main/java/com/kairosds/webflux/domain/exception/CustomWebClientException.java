package com.kairosds.webflux.domain.exception;

import java.io.Serial;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClientException;

@Getter
public class CustomWebClientException extends WebClientException {

  @Serial
  private static final long serialVersionUID = -6034091653862477873L;

  private final String clientName;

  public CustomWebClientException(@NonNull String clientName, Throwable ex) {
    super("Error when calling to ".concat(clientName), ex);
    this.clientName = clientName;

  }
}

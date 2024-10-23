package com.kairosds.webflux.domain.exception;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 4847713926331520504L;

  private String entityId;

  private String entityName;
}

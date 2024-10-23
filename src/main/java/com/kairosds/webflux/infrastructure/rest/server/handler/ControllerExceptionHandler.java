package com.kairosds.webflux.infrastructure.rest.server.handler;

import com.kairosds.webflux.domain.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {
    log.warn("[START]", ex);
    final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        "Entity '%s' with id '%s' was not found".formatted(ex.getEntityName(), ex.getEntityId()));
    pd.setProperty(ex.getEntityName(), ex.getEntityId());
    return pd;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception ex) {
    log.error("[ERROR] Unexpected error.", ex);
    final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    return pd;
  }
}

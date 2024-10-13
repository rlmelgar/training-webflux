package com.kairosds.webflux.basic.c_mono;

import java.util.Optional;

import reactor.core.publisher.Mono;

public class Mono1Create {

  public static void main(String[] args) {
    getFromStringOrEmpty(null).subscribe(stringVal -> System.out.println("getFromString: " + stringVal));
    getFromString("Mario Bros").subscribe(stringVal -> System.out.println("getFromString: " + stringVal));
    getFromOptional(Optional.of("Mario")).subscribe(stringVal -> System.out.println("getFromOptional: " + stringVal));
    getFromOptional(Optional.empty()).subscribe(stringVal -> System.out.println("getFromOptional empty: " + stringVal));
    getEmpty().subscribe(stringVal -> System.out.println("getEmpty: " + stringVal));
    getError().subscribe(stringVal -> System.out.println("getError: " + stringVal),
        throwable -> System.out.println("getError: " + throwable.getMessage()));

    getFromObject(Optional.empty()).subscribe(stringVal -> System.out.println("getFromObject: " + stringVal));

    try {
      Thread.sleep(5000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static Mono<Mono<String>> getFromFlux(String text) {
    return Mono.just(Mono.just(text));
  }

  private static Mono<Optional<String>> getFromObject(Optional<String> object) {
    return Mono.just(object);
  }

  private static Mono<String> getFromString(String text) {
    return Mono.just(text);
  }

  private static Mono<String> getFromStringOrEmpty(String text) {
    return Mono.justOrEmpty(text);
  }

  private static Mono<String> getFromOptional(Optional<String> optionalString) {
    return Mono.justOrEmpty(optionalString);
  }

  private static Mono<Object> getEmpty() {
    return Mono.empty();
  }

  private static Mono<Object> getError() {
    return Mono.error(new Exception("error mono"));
  }
}

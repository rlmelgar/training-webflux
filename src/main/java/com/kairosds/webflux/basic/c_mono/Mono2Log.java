package com.kairosds.webflux.basic.c_mono;

import reactor.core.publisher.Mono;

public class Mono2Log {

  public static void main(String[] args) {
    final Mono<String> stringMono = showEvents("Mario");

    stringMono.subscribe(s -> System.out.println("Sus1:" + s));
    stringMono.subscribe(s -> System.out.println("Sus2:" + s));

    try {
      Thread.sleep(10000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static Mono<String> showEvents(String string) {
    return Mono.just(string)
        .switchIfEmpty(Mono.error(NullPointerException::new))
        .log("MONO BUENO");

  }

}

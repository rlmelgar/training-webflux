package com.kairosds.webflux.basic.b_flux;

import java.util.List;

import reactor.core.publisher.Flux;

public class Flux2Log {

  public static void main(String[] args) {
    final Flux<String> stringFlux = showEvents(List.of("Mario", "Luigi"));

    stringFlux.subscribe(s -> System.out.println("Sus1:" + s));
    stringFlux.subscribe(s -> System.out.println("Sus2:" + s));

    try {
      Thread.sleep(10000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static Flux<String> showEvents(List<String> stringList) {
    return Flux.fromIterable(stringList)
        .switchIfEmpty(Flux.error(NullPointerException::new))
        .log();

  }

}

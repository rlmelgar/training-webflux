package com.kairosds.webflux.a_intro;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class Intro1Reactive {

  public static void main(String[] args) {
    final Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(2));
    integerFlux.subscribe(integer -> System.out.println("First: " + integer));
    integerFlux.subscribe(integer -> System.out.println("Second: " + integer));

    try {
      Thread.sleep(5000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

}

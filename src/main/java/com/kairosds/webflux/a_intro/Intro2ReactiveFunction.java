package com.kairosds.webflux.a_intro;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Intro2ReactiveFunction {

  public static void main(String[] args) {
    final Intro2ReactiveFunction intro2_reactiveFunction = new Intro2ReactiveFunction();
    intro2_reactiveFunction.getIntegers().delayElements(Duration.ofSeconds(1))
        .subscribe(integer -> System.out.println("First: " + integer));
    intro2_reactiveFunction.getIntegers().subscribe(integer -> System.out.println("Second: " + integer));
    intro2_reactiveFunction.getString("String").subscribe(stringVal -> System.out.println("Mono: " + stringVal));

    try {
      Thread.sleep(10000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  public Flux<Integer> getIntegers() {
    return Flux.just(1, 2, 3, 4, 5);
  }

  public Mono<String> getString(String value) {
    return Mono.just(value).delayElement(Duration.ofSeconds(2));
  }

}

package com.kairosds.webflux.a_intro;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class Intro1ReactiveTest {

  @Test
  void whenReceiveFromOneToFiveThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5)
        .delayElements(Duration.ofSeconds(1));

    // THEN
    StepVerifier.create(integerFlux.log())
        .expectNext(1)
        .expectNext(2)
        .expectNext(3)
        .expectNext(4)
        .expectNext(5)
        .verifyComplete();
  }
}

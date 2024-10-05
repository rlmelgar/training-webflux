package com.kairosds.webflux.a_intro;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Intro2ReactiveFunctionTest {

  Intro2ReactiveFunction intro2_reactiveFunction = new Intro2ReactiveFunction();

  @Test
  void whenReceiveStringThenReturnIt() {
    // GIVEN
    final String stringValue = "String";

    // WHEN
    final Mono<String> integerFlux = this.intro2_reactiveFunction.getString(stringValue);

    // THEN
    StepVerifier.create(integerFlux.log("testing"))
        .recordWith(ArrayList::new)
        .expectNextCount(1)
        .consumeRecordedWith(persons -> assertThat(persons).allMatch(value -> value.startsWith("S")))
        .expectComplete()
        .verify();
  }

  @Test
  void whenReceiveIntegersThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Integer> integerFlux = this.intro2_reactiveFunction.getIntegers();

    // THEN
    StepVerifier.withVirtualTime(() -> integerFlux.log(), 1)
        .expectSubscription()
        .expectNoEvent(Duration.ofSeconds(1))
        .expectNextCount(1)
        .thenRequest(3)
        .expectNextCount(3)
        .thenRequest(Long.MAX_VALUE)
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void whenReceiveDelayedStringThenReturnItLater() {
    // GIVEN

    // WHEN
    final Mono<String> integerFlux = this.intro2_reactiveFunction.getString("delayed");

    // THEN
    StepVerifier.withVirtualTime(integerFlux::log, 1)
        .expectSubscription()
        .expectNoEvent(Duration.ofSeconds(1))
        .thenAwait(Duration.ofSeconds(1))
        .thenRequest(1)
        .expectNextCount(1)
        .verifyComplete();
  }
}

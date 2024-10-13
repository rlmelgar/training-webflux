package com.kairosds.webflux.basic.e_combinations;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.domain.model.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FirstWithSignalTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenUseOrThenReturnTheFasterIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.just("START", "STOP").delayElements(Duration.ofSeconds(0))
        .or(Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("START")
        .expectNext("STOP")
        .verifyComplete();
  }

  @Test
  void whenUseOrAndTheFasterIsEmptyThenReturnEmpty() {
    // GIVEN

    // WHEN
    final Mono<String> stringMono = Mono.just("START").delayElement(Duration.ofSeconds(1))
        .or(Mono.empty().cast(String.class).delayElement(Duration.ofSeconds(0)));

    // THEN
    StepVerifier.create(stringMono)
        .verifyComplete();
  }

  @Test
  void whenUseFirstWithSignalThenReturnTheFasterIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.firstWithSignal(
        Flux.just("START", "STOP").delayElements(Duration.ofSeconds(0)),
        Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("START")
        .expectNext("STOP")
        .verifyComplete();
  }

  @Test
  void whenUseFirstWithSignalIsErrorThenReturnTheError() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.firstWithSignal(
        Flux.error(RuntimeException::new).cast(String.class).delayElements(Duration.ofSeconds(0)),
        Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void whenUseFirstWithValueThenReturnTheFasterIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.firstWithValue(
        Flux.error(RuntimeException::new).cast(String.class).delayElements(Duration.ofSeconds(0)),
        Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("GO")
        .expectNext("RETURN")
        .verifyComplete();
  }
}

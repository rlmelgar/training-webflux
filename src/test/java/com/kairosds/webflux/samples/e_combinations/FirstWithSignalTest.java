package com.kairosds.webflux.samples.e_combinations;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FirstWithSignalTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenUseOrThenReturnTheFasterIt() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux = Flux.just("START", "STOP").delayElements(Duration.ofSeconds(0))
        .or(Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
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
    final Flux<String> CharacterRecordFlux = Flux.firstWithSignal(
        Flux.just("START", "STOP").delayElements(Duration.ofSeconds(0)),
        Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext("START")
        .expectNext("STOP")
        .verifyComplete();
  }

  @Test
  void whenUseFirstWithSignalIsErrorThenReturnTheError() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux = Flux.firstWithSignal(
        Flux.error(RuntimeException::new).cast(String.class).delayElements(Duration.ofSeconds(0)),
        Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void whenUseFirstWithValueThenReturnTheFasterIt() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux = Flux.firstWithValue(
        Flux.error(RuntimeException::new).cast(String.class).delayElements(Duration.ofSeconds(0)),
        Flux.just("GO", "RETURN").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext("GO")
        .expectNext("RETURN")
        .verifyComplete();
  }
}

package com.kairosds.webflux.e_combinations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kairosds.webflux.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

class Flux1RepeatAndRetryTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenTransformWithRepeatWithThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .repeat();

    // THEN
    StepVerifier.create(characterMono)
        .expectNextCount(4)
        .expectNext(Character.getMario())
        .expectNextCount(7)
        .expectNext(Character.getMario())
        .thenCancel()
        .verify();
  }

  @Test
  void whenTransformWithRepeat2WithThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .repeat(1);

    // THEN
    StepVerifier.create(characterMono)
        .expectNextCount(8)
        .verifyComplete();
  }

  @Test
  void whenTransformWithRepeat3WithThenReturnIt() {
    // GIVEN
    final List<Character> list = new ArrayList<>();

    // WHEN
    final Flux<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(character -> {
          list.add(character);
          return character;
        })
        .repeat(100, () -> list.size() < 10);

    // THEN
    StepVerifier.create(characterMono)
        .expectNextCount(12)
        .verifyComplete();
  }

  @Test
  void whenTransformWithRepeatWhenWithThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.just(Character.getMario())
        .repeatWhen(longFlux -> longFlux.map(this::storageNotFull))
        .flatMap(this::storeCharacter);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNextCount(10)
        .expectError(RuntimeException.class)
        .verify();
  }

  private Flux<Character> storeCharacter(Character character) {
    return Flux.just(character);
  }

  private long storage = 0;

  private Flux<String> storageNotFull(long elements) {
    this.storage += elements;
    if (this.storage > 9) {
      throw new RuntimeException("Storage full!");
    }
    return Flux.just("Storage left!");
  }

  @Test
  void whenTransformWithRetryThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.just(Character.getBowser())
        .flatMap(this::removeOneLife)
        .retry(10);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  private Mono<Character> removeOneLife(Character character) {
    final int n = new Random().nextInt(50);

    if (n > 2) {
      System.out.println("Resource not available yet for ".concat(character.name()));
      return Mono.error(new RuntimeException("Resource not available yet for ".concat(character.name())));
    }
    System.out.println("Resource OK for ".concat(character.name()));
    return Mono.just(character);
  }

  @Test
  void whenTransformWithRetryWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMap(this::removeOneLife)
        .retryWhen(Retry.fixedDelay(50, Duration.ofMillis(10)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNextCount(4)
        .verifyComplete();
  }
}

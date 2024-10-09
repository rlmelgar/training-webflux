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

class RepeatAndRetryTest {

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
    final long repeats = 10;

    // WHEN
    final Flux<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .repeat(repeats);

    // THEN
    StepVerifier.create(characterMono)
        .expectNextCount((1 + repeats) * SUPER_MARIO_CHARACTERS.size())
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
      System.out.println("Storage full!");
      throw new RuntimeException("Storage full!");
    }
    System.out.println("Storage left!");
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

  @Test
  void whenGiveMeFastestWhenThenReturnTheFastest() {
    // GIVEN
    final long repeats = 10;
    final int delay1 = new Random().nextInt(3);

    int delay2 = new Random().nextInt(3);

    while (delay1 == delay2) {
      delay2 = new Random().nextInt(3);
    }
    final long expectedCount = delay1 < delay2 ? 4 * (repeats + 1) : (repeats + 1);

    System.out.println("Delay 1:" + delay1 + ", Delay 2:" + delay2);
    System.out.println("expectedCount " + expectedCount);
    // WHEN
    final Flux<Character> characterFlux = this.giveMeFastest(repeats, delay1, delay2);

    // THEN
    StepVerifier.create(characterFlux.log())
        .expectNextCount(expectedCount)
        .verifyComplete();
  }

  private Flux<Character> giveMeFastest(long repeats, int delay1, int delay2) {

    return Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(delay1 * 10L))
        .or(Mono.just(Character.getBowser()).delayElement(Duration.ofMillis(delay2 * 10L)))
        .repeat(repeats);
  }

}

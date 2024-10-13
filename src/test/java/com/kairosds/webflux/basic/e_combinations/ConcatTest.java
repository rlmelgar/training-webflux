package com.kairosds.webflux.basic.e_combinations;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.domain.model.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ConcatTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenConcatWithThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatWith(Mono.just(Character.getBowser()));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getMario())
        .expectNextCount(3)
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenConcatWithPublisherWithErrorThenReturnFirstPublisherAndError() {
    // GIVEN

    // WHEN
    final Flux<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatWith(Mono.error(RuntimeException::new));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getMario())
        .expectNextCount(3)
        .verifyError(RuntimeException.class);
  }

  @Test
  void whenConcatWithValuesThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatWithValues(Character.getBowser());

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getMario())
        .expectNextCount(3)
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenConcatThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.concat(Flux.fromIterable(SUPER_MARIO_CHARACTERS), Mono.just(Character.getBowser()));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNextCount(3)
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenStartWithPublisherThenReturnBothPublishersStartingWithIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux =
        Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000))
            .startWith(Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getBowser())
        .expectNext(Character.getBowser())
        .expectNextCount(4)
        .verifyComplete();
  }

}

package com.kairosds.webflux.d_transformations;

import static com.kairosds.webflux.Character.LUIGI_NAME;
import static com.kairosds.webflux.Character.MARIO_NAME;
import static com.kairosds.webflux.Character.PEACH_NAME;
import static com.kairosds.webflux.Character.TOAD_NAME;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import com.kairosds.webflux.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

class Flux4TransformationsTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenTransformWithSingleThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .single();

    // THEN
    StepVerifier.create(characterMono)
        .expectError()
        .verify();
  }

  @Test
  void whenTransformWithSingle2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filter(character -> character.height() > 10)
        .single(Character.getBowser());

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSingleOrEmptyThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filter(character -> character.height() > 10)
        .singleOrEmpty();

    // THEN
    StepVerifier.create(characterMono)
        .verifyComplete();
  }

  @Test
  void whenTransformWithSkipThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skip(2);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSkipLastThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skipLast(2);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSkipUntilThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skipUntil(character -> character.prefix().equals("MISS"));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSkipWhileThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skipWhile(character -> character.prefix().equals("SR"));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSkipUntilOtherThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .delayElements(Duration.ofSeconds(2))
        .skipUntilOther(Mono.just(Character.getBowser()).delayElement(Duration.ofSeconds(5)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSortThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .sort(Comparator.comparing(Character::name));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getLuigi())
        .expectNext(Character.getMario())
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithThenThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .then();

    // THEN
    StepVerifier.create(characterFlux)
        .verifyComplete();

  }

  @Test
  void whenTransformWithThen2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .then(Mono.just(Character.getBowser()));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getBowser())
        .verifyComplete();

  }

  @Test
  void whenTransformWithThenEmptyThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> characterFlux = Flux.just("SUPER MARIO WORLD")
        .thenEmpty(Flux.fromIterable(SUPER_MARIO_CHARACTERS).then());

    // THEN
    StepVerifier.create(characterFlux)
        .verifyComplete();

  }

  @Test
  void whenTransformWithThenManyThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.just("SUPER MARIO WORLD")
        .thenMany(Flux.fromIterable(SUPER_MARIO_CHARACTERS));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNextCount(4)
        .verifyComplete();

  }

  @Test
  void whenTransformWithTransformThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux1 = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMap(character -> this.toName(character));
    final Flux<String> characterFlux2 = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .transform(character -> character.flatMap(this::toName));
    final Flux<String> characterFlux3 = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .transformDeferred(character -> character.flatMap(this::toName));
    final Flux<String> characterFlux4 = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .transformDeferredContextual((characterFlux, contextView) -> characterFlux
            .log("for RequestID->" + contextView.get("RequestID"))
            .flatMap(this::toName))
        .contextWrite(Context.of("RequestID", "characterFlux4"));

    // THEN
    StepVerifier.create(characterFlux4)
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(PEACH_NAME)
        .expectNext(TOAD_NAME)
        .verifyComplete();
    // THEN

  }

  private Mono<String> toName(Character character) {
    return Mono.just(character.name());
  }

  @Test
  void whenTransformWithOrThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.just("START").delayElements(Duration.ofSeconds(0))
        .or(Flux.just("STOP").delayElements(Duration.ofSeconds(1)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("START")
        .verifyComplete();

  }
}

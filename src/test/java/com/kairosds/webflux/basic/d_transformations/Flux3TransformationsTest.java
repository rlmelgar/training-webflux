package com.kairosds.webflux.basic.d_transformations;

import static com.kairosds.webflux.domain.model.Character.LUIGI_NAME;
import static com.kairosds.webflux.domain.model.Character.MARIO_NAME;
import static com.kairosds.webflux.domain.model.Character.PEACH_NAME;
import static com.kairosds.webflux.domain.model.Character.TOAD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.kairosds.webflux.domain.model.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

class Flux3TransformationsTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenTransformWithHasElementThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Boolean> booleanMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .hasElement(Character.getMario());

    // THEN
    StepVerifier.create(booleanMono)
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void whenTransformWithHasElementsThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Boolean> booleanMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .hasElements();

    // THEN
    StepVerifier.create(booleanMono)
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void whenTransformWithIndexThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Tuple2<Long, Character>> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .index((aLong, character) -> Tuples.of(Long.valueOf(character.id()), character));

    // THEN
    StepVerifier.create(resultFlux)
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(1L, Character.getMario())))
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(2L, Character.getLuigi())))
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(3L, Character.getPeach())))
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(4L, Character.getToad())))
        .verifyComplete();
  }

  @Test
  void whenTransformWithLastThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .last(Character.getMario());

    // THEN
    StepVerifier.create(resultFlux)
        .expectNext(Character.getToad())
        .verifyComplete();
  }

  @Test
  void whenTransformWithMapThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(Character::name);

    // THEN
    StepVerifier.create(resultFlux)
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(PEACH_NAME)
        .expectNext(TOAD_NAME)
        .verifyComplete();
  }

  @Test
  void whenTransformWithMapNotNullThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> resultFlux = Flux.just(new Character("5", null, null, 2))
        .mapNotNull(Character::name);

    // THEN
    StepVerifier.create(resultFlux)
        .verifyComplete();
  }

  @Test
  void whenTransformWithNextThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .next();

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithOfTypeThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterMono = Flux.just("0", false, 2L, "Tree")
        .ofType(String.class);

    // THEN
    StepVerifier.create(characterMono)
        .expectNext("0")
        .expectNext("Tree")
        .verifyComplete();
  }

  @Test
  void whenTransformWithReduceThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Integer> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(Character::height)
        .reduce(10, Integer::sum);

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(17)
        .verifyComplete();
  }

  @Test
  void whenTransformWithReduceWithThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .reduceWith(Character::getBowser,
            (bowser, character) -> Character.getBowser(bowser.life() - character.height()));

    // THEN
    StepVerifier.create(characterMono)
        .assertNext(bowser -> assertThat(bowser).extracting("life").isEqualTo(3))
        .verifyComplete();
  }

  @Test
  void whenTransformWithScanThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Integer> integerFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(Character::height)
        .scan(10, Integer::sum);

    // THEN
    StepVerifier.create(integerFlux)
        .expectNext(10)
        .expectNext(12)
        .expectNext(14)
        .expectNext(16)
        .expectNext(17)
        .verifyComplete();
  }

  @Test
  void whenTransformWithScanWithThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .scanWith(Character::getBowser,
            (bowser, character) -> Character.getBowser(bowser.life() - character.height()));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getBowser())
        .expectNext(Character.getBowser(8))
        .expectNext(Character.getBowser(6))
        .expectNext(Character.getBowser(4))
        .expectNext(Character.getBowser(3))
        .verifyComplete();
  }

  @Test
  void whenTransformWithReduceToStringThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<String> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(character -> character.id().concat("-").concat(character.name()))
        .reduce((characterString, characterString2) -> characterString.concat(";").concat(characterString2));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext("1-Mario;2-Luigi;3-Peach;4-Toad")
        .verifyComplete();
  }
}

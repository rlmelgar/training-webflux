package com.kairosds.webflux.basic.e_combinations;

import static com.kairosds.webflux.domain.model.Character.BOWSER_NAME;
import static com.kairosds.webflux.domain.model.Character.MARIO_NAME;

import java.util.List;

import com.kairosds.webflux.domain.model.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

class ZipTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenZipWithThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Tuple2<Character, Character>> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWith(Flux.just(Character.getBowser()));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Tuples.of(Character.getMario(), Character.getBowser()))
        .verifyComplete();
  }

  @Test
  void whenZipWithUsingFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWith(Flux.just(Character.getBowser()), (character, character2) -> character.name().concat(character2.name()));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(MARIO_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

  @Test
  void whenZipThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Tuple2<Character, Character>> characterMono = Flux
        .zip(Flux.fromIterable(SUPER_MARIO_CHARACTERS), Flux.just(Character.getBowser()));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Tuples.of(Character.getMario(), Character.getBowser()))
        .verifyComplete();
  }

  @Test
  void whenZipUsingFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> characterMono = Flux
        .zip(Flux.fromIterable(SUPER_MARIO_CHARACTERS), Flux.just(Character.getBowser()),
            (character, character2) -> character.name().concat(character2.name()));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(MARIO_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

  @Test
  void whenZipWithIterableFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Tuple2<Character, Character>> tuple2Flux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWithIterable(List.of(Character.getBowser()));

    // THEN
    StepVerifier.create(tuple2Flux)
        .expectNext(Tuples.of(Character.getMario(), Character.getBowser()))
        .verifyComplete();
  }

  @Test
  void whenZipWithIterableUsingFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWithIterable(List.of(Character.getBowser()), (character, character2) -> character.name().concat(character2.name()));

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(MARIO_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

}

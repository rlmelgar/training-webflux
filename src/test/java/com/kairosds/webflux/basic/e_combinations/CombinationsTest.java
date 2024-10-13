package com.kairosds.webflux.basic.e_combinations;

import static com.kairosds.webflux.domain.model.Character.BOWSER_NAME;
import static com.kairosds.webflux.domain.model.Character.LUIGI_NAME;
import static com.kairosds.webflux.domain.model.Character.PEACH_NAME;
import static com.kairosds.webflux.domain.model.Character.TOAD_NAME;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.domain.model.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class CombinationsTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenMergeWithThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000))
        .mergeWith(Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .expectNext(Character.getBowser())
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenMergeThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux =
        Flux.merge(Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .expectNext(Character.getBowser())
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenMergePriorityThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux =
        Flux.mergePriority((a, b) -> b.id().compareTo(a.id()),
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .expectNext(Character.getBowser())
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenMergeComparingThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux =
        Flux.mergeComparing((a, b) -> b.id().compareTo(a.id()),
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getBowser())
        .expectNext(Character.getBowser())
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .verifyComplete();
  }

  @Test
  void whenMergeSequentialThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux =
        Flux.mergeSequential(
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .expectNext(Character.getPeach())
        .expectNext(Character.getToad())
        .expectNext(Character.getBowser())
        .expectNext(Character.getBowser())
        .verifyComplete();
  }

  @Test
  void whenCombineLatestThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux =
        Flux.combineLatest(
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3000)),
            (character, character2) -> character.name().concat(character2.name()));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(LUIGI_NAME.concat(BOWSER_NAME))
        .expectNext(PEACH_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

  @Test
  void whenWithLatestFromThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux =
        Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000))
            .withLatestFrom(
                Flux.just(Character.getBowser(), Character.getBowser()).delayElements(Duration.ofMillis(3100)),
                (character, character2) -> character.name().concat(character2.name()));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(TOAD_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

}

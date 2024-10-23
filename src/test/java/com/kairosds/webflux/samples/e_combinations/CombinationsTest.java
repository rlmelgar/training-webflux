package com.kairosds.webflux.samples.e_combinations;

import static com.kairosds.webflux.samples.CharacterRecord.BOWSER_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.LUIGI_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.PEACH_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.TOAD_NAME;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class CombinationsTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenMergeWithThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000))
        .mergeWith(Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenMergeThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux =
        Flux.merge(Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenMergePriorityThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux =
        Flux.mergePriority((a, b) -> b.id().compareTo(a.id()),
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenMergeComparingThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux =
        Flux.mergeComparing((a, b) -> b.id().compareTo(a.id()),
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .verifyComplete();
  }

  @Test
  void whenMergeSequentialThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux =
        Flux.mergeSequential(
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenCombineLatestThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux =
        Flux.combineLatest(
            Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000)),
            Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()).delayElements(Duration.ofMillis(2800)),
            (CharacterRecord, CharacterRecord2) -> CharacterRecord.name().concat(CharacterRecord2.name()));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(LUIGI_NAME.concat(BOWSER_NAME))
        .expectNext(PEACH_NAME.concat(BOWSER_NAME))
        .expectNext(TOAD_NAME.concat(BOWSER_NAME))
        .expectNext(TOAD_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

  @Test
  void whenWithLatestFromThenReturnTailoredPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux =
        Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(600))
            .withLatestFrom(
                Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowserB()).delayElements(Duration.ofMillis(1000)),
                (CharacterRecord, CharacterRecord2) -> CharacterRecord.name().concat(CharacterRecord2.name()));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(LUIGI_NAME.concat(BOWSER_NAME))
        .expectNext(PEACH_NAME.concat(BOWSER_NAME))
        .expectNext(TOAD_NAME.concat(BOWSER_NAME.concat("B")))
        .verifyComplete();
  }

}

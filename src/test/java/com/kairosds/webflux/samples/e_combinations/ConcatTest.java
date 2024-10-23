package com.kairosds.webflux.samples.e_combinations;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ConcatTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenConcatWithThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatWith(Mono.just(CharacterRecord.getBowser()));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getMario())
        .expectNextCount(3)
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenConcatWithPublisherWithErrorThenReturnFirstPublisherAndError() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatWith(Mono.error(RuntimeException::new));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getMario())
        .expectNextCount(3)
        .verifyError(RuntimeException.class);
  }

  @Test
  void whenConcatWithValuesThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatWithValues(CharacterRecord.getBowser());

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getMario())
        .expectNextCount(3)
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenConcatThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux =
        Flux.concat(Flux.fromIterable(SUPER_MARIO_CHARACTERS), Mono.just(CharacterRecord.getBowser()));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNextCount(3)
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenStartWithPublisherThenReturnBothPublishersStartingWithIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux =
        Flux.fromIterable(SUPER_MARIO_CHARACTERS).delayElements(Duration.ofMillis(1000))
            .startWith(Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()).delayElements(Duration.ofMillis(3000)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getBowser())
        .expectNextCount(4)
        .verifyComplete();
  }

}

package com.kairosds.webflux.samples.e_combinations;

import static com.kairosds.webflux.samples.CharacterRecord.BOWSER_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.MARIO_NAME;

import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

class ZipTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenZipWithThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Tuple2<CharacterRecord, CharacterRecord>> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWith(Flux.just(CharacterRecord.getBowser()));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(Tuples.of(CharacterRecord.getMario(), CharacterRecord.getBowser()))
        .verifyComplete();
  }

  @Test
  void whenZipWithUsingFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWith(Flux.just(CharacterRecord.getBowser()),
            (CharacterRecord, CharacterRecord2) -> CharacterRecord.name().concat(CharacterRecord2.name()));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(MARIO_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

  @Test
  void whenZipThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Tuple2<CharacterRecord, CharacterRecord>> CharacterRecordMono = Flux
        .zip(Flux.fromIterable(SUPER_MARIO_CHARACTERS), Flux.just(CharacterRecord.getBowser()));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(Tuples.of(CharacterRecord.getMario(), CharacterRecord.getBowser()))
        .verifyComplete();
  }

  @Test
  void whenZipUsingFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordMono = Flux
        .zip(Flux.fromIterable(SUPER_MARIO_CHARACTERS), Flux.just(CharacterRecord.getBowser()),
            (CharacterRecord, CharacterRecord2) -> CharacterRecord.name().concat(CharacterRecord2.name()));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(MARIO_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

  @Test
  void whenZipWithIterableFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<Tuple2<CharacterRecord, CharacterRecord>> tuple2Flux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWithIterable(List.of(CharacterRecord.getBowser()));

    // THEN
    StepVerifier.create(tuple2Flux)
        .expectNext(Tuples.of(CharacterRecord.getMario(), CharacterRecord.getBowser()))
        .verifyComplete();
  }

  @Test
  void whenZipWithIterableUsingFunctionThenReturnBothPublishers() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .zipWithIterable(List.of(CharacterRecord.getBowser()),
            (CharacterRecord, CharacterRecord2) -> CharacterRecord.name().concat(CharacterRecord2.name()));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(MARIO_NAME.concat(BOWSER_NAME))
        .verifyComplete();
  }

}

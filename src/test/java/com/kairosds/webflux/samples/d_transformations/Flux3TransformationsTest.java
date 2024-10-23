package com.kairosds.webflux.samples.d_transformations;

import static com.kairosds.webflux.samples.CharacterRecord.LUIGI_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.MARIO_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.PEACH_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.TOAD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

class Flux3TransformationsTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenTransformWithHasElementThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Boolean> booleanMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .hasElement(CharacterRecord.getMario());

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
    final Flux<Tuple2<Long, CharacterRecord>> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .index((aLong, CharacterRecord) -> Tuples.of(Long.valueOf(CharacterRecord.id()), CharacterRecord));

    // THEN
    StepVerifier.create(resultFlux)
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(1L, CharacterRecord.getMario())))
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(2L, CharacterRecord.getLuigi())))
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(3L, CharacterRecord.getPeach())))
        .assertNext(tuple -> assertThat(tuple).isEqualTo(Tuples.of(4L, CharacterRecord.getToad())))
        .verifyComplete();
  }

  @Test
  void whenTransformWithLastThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .last(CharacterRecord.getMario());

    // THEN
    StepVerifier.create(resultFlux)
        .expectNext(CharacterRecord.getToad())
        .verifyComplete();
  }

  @Test
  void whenTransformWithMapThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(CharacterRecord::name);

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
    final Flux<String> resultFlux = Flux.just(new CharacterRecord("5", null, null, 2, 2))
        .mapNotNull(CharacterRecord::name);

    // THEN
    StepVerifier.create(resultFlux)
        .verifyComplete();
  }

  @Test
  void whenTransformWithNextThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .next();

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithOfTypeThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordMono = Flux.just("0", false, 2L, "Tree")
        .ofType(String.class);

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext("0")
        .expectNext("Tree")
        .verifyComplete();
  }

  @Test
  void whenTransformWithReduceThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Integer> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(CharacterRecord::height)
        .reduce(10, Integer::sum);

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(17)
        .verifyComplete();
  }

  @Test
  void whenTransformWithReduceWithThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .reduceWith(CharacterRecord::getBowser,
            (bowser, CharacterRecord) -> com.kairosds.webflux.samples.CharacterRecord
                .getBowser(bowser.life() - CharacterRecord.height()));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .assertNext(bowser -> assertThat(bowser).extracting("life").isEqualTo(3))
        .verifyComplete();
  }

  @Test
  void whenTransformWithScanThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Integer> integerFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(CharacterRecord::height)
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
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .scanWith(CharacterRecord::getBowser,
            (bowser, CharacterRecord) -> com.kairosds.webflux.samples.CharacterRecord
                .getBowser(bowser.life() - CharacterRecord.height()));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getBowser())
        .expectNext(CharacterRecord.getBowser(8))
        .expectNext(CharacterRecord.getBowser(6))
        .expectNext(CharacterRecord.getBowser(4))
        .expectNext(CharacterRecord.getBowser(3))
        .verifyComplete();
  }

  @Test
  void whenTransformWithReduceToStringThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<String> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .map(CharacterRecord -> CharacterRecord.id().concat("-").concat(CharacterRecord.name()))
        .reduce((CharacterRecordString, CharacterRecordString2) -> CharacterRecordString.concat(";").concat(CharacterRecordString2));

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext("1-Mario;2-Luigi;3-Peach;4-Toad")
        .verifyComplete();
  }
}

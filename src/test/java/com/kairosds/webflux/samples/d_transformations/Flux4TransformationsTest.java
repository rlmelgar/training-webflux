package com.kairosds.webflux.samples.d_transformations;

import static com.kairosds.webflux.samples.CharacterRecord.LUIGI_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.MARIO_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.PEACH_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.TOAD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

class Flux4TransformationsTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenTransformWithSingleThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .single();

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectError()
        .verify();
  }

  @Test
  void whenTransformWithSingle2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filter(CharacterRecord -> CharacterRecord.height() > 10)
        .single(CharacterRecord.getBowser());

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSingleOrEmptyThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filter(CharacterRecord -> CharacterRecord.height() > 10)
        .singleOrEmpty();

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .verifyComplete();
  }

  @Test
  void whenTransformWithSkipThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skip(2);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSkipLastThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skipLast(2);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSkipUntilThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skipUntil(CharacterRecord -> CharacterRecord.prefix().equals("MISS"));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSkipWhileThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .skipWhile(CharacterRecord -> CharacterRecord.prefix().equals("SR"));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSkipUntilOtherThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .delayElements(Duration.ofSeconds(2))
        .skipUntilOther(Mono.just(CharacterRecord.getBowser()).delayElement(Duration.ofSeconds(5)));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithSortThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .sort(Comparator.comparing(CharacterRecord::name));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getPeach())
        .expectNext(CharacterRecord.getToad())
        .verifyComplete();

  }

  @Test
  void whenTransformWithThenThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .then();

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .verifyComplete();

  }

  @Test
  void whenTransformWithThen2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .then(Mono.just(CharacterRecord.getBowser()));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getBowser())
        .verifyComplete();

  }

  @Test
  void whenTransformWithThenEmptyThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> CharacterRecordFlux = Flux.just("SUPER MARIO WORLD")
        .thenEmpty(Flux.fromIterable(SUPER_MARIO_CHARACTERS).then());

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .verifyComplete();

  }

  @Test
  void whenTransformWithThenManyThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.just("SUPER MARIO WORLD")
        .thenMany(Flux.fromIterable(SUPER_MARIO_CHARACTERS));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNextCount(4)
        .verifyComplete();

  }

  @Test
  void whenTransformWithTransformThenReturnIt() {
    // GIVEN
    final List<String> SUPER_MARIO_NAMES = List.of(MARIO_NAME, LUIGI_NAME, PEACH_NAME, TOAD_NAME);

    // WHEN
    final Flux<String> fluxWithFlatMap = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMap(CharacterRecord -> this.toName(CharacterRecord));
    final Flux<String> fluxWithTransform = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .transform(CharacterRecord -> CharacterRecord.flatMap(this::toName));
    final Flux<String> fluxWithTransformDeferred = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .transformDeferred(CharacterRecord -> CharacterRecord.flatMap(this::toName));
    final Flux<String> fluxWithTransformDeferredContextual = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .transformDeferredContextual(
            (CharacterRecordFlux, contextView) -> CharacterRecordFlux.log("for RequestID->" + contextView.get("RequestID"))
                .flatMap(this::toName))
        .contextWrite(Context.of("RequestID", "CharacterRecordFlux4"));

    // THEN
    StepVerifier.create(fluxWithTransform)
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .verifyComplete();
    // THEN

  }

  private Mono<String> toName(CharacterRecord CharacterRecord) {
    return Mono.just(CharacterRecord.name()).delayElement(Duration.ofSeconds(10));
  }

  @Test
  void whenTransformWithConcatMapThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> fluxWithConcatMap = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatMap(CharacterRecord -> this.toName(CharacterRecord));

    // THEN
    StepVerifier.create(fluxWithConcatMap.log())
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(PEACH_NAME)
        .expectNext(TOAD_NAME)
        .verifyComplete();
  }

  @Test
  void whenTransformWithConcatMapIterableWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> fluxWithConcatMapIterable = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatMapIterable(CharacterRecord -> List.of(CharacterRecord.id(), CharacterRecord.name()));

    // THEN
    StepVerifier.create(fluxWithConcatMapIterable.log())
        .expectNext("1", MARIO_NAME)
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  void whenTransformComparingDifferentOptionsThenReturnIt() {
    // GIVEN
    final List<String> SUPER_MARIO_NAMES = List.of(MARIO_NAME, LUIGI_NAME, PEACH_NAME, TOAD_NAME);

    // WHEN
    final Flux<String> fluxWithFlatMap = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMap(CharacterRecord -> this.toName(CharacterRecord));
    final Flux<String> fluxWithFlatMapSequential = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMapSequential(CharacterRecord -> this.toName(CharacterRecord));
    final Flux<String> fluxWithConcatMap = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .concatMap(CharacterRecord -> this.toName(CharacterRecord));
    final Flux<String> fluxWithTransform = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .transform(CharacterRecord -> CharacterRecord.flatMap(this::toName));

    // THEN
    StepVerifier.create(fluxWithFlatMap.log("<FLATMAP>"))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .verifyComplete();
    // THEN
    StepVerifier.create(fluxWithFlatMapSequential.log("<FLATMAP_SEQUENTIAL>"))
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(PEACH_NAME)
        .expectNext(TOAD_NAME)
        .verifyComplete();
    // THEN
    StepVerifier.create(fluxWithConcatMap.log("<CONCATMAP>"))
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(PEACH_NAME)
        .expectNext(TOAD_NAME)
        .verifyComplete();
    // THEN
    StepVerifier.create(fluxWithTransform.log("<TRANSFORM>"))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .assertNext(name -> assertThat(SUPER_MARIO_NAMES).contains(name))
        .verifyComplete();

  }

  Mono<Void> deleteCharacterRecords(List<CharacterRecord> CharacterRecordList) {
    return Flux.fromIterable(CharacterRecordList)
        .flatMap(CharacterRecord -> this.CharacterRecordPersistenceService.deleteScore(CharacterRecord)
            .thenEmpty(this.CharacterRecordPersistenceService.deleteCharacterRecord(CharacterRecord)))
        .then();
  }

  CharacterRecordPersistenceService CharacterRecordPersistenceService = new CharacterRecordPersistenceService();

  class CharacterRecordPersistenceService {

    public Mono<Void> deleteCharacterRecord(CharacterRecord CharacterRecord) {
      return Mono.just(CharacterRecord)
          .then(Mono.just("sss").then());
    }

    public Mono<Void> deleteScore(CharacterRecord CharacterRecord) {
      return Mono.empty();
    }
  }

}

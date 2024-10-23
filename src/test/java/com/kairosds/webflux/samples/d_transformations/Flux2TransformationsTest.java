package com.kairosds.webflux.samples.d_transformations;

import static com.kairosds.webflux.samples.CharacterRecord.LUIGI_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.MARIO_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.PEACH_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.TOAD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Flux2TransformationsTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenTransformWithDistinctThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .distinct(CharacterRecord::name, HashSet::new, (objects, s) -> objects.add(s.substring(2, 3)),
            CharacterRecordsHashSetKeys -> CharacterRecordsHashSetKeys.forEach(object -> System.out.println("Keys:" + object)));

    // THEN
    StepVerifier.create(resultFlux)
        .expectNextSequence(List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithDistinctUntilChangedThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> resultFlux = Flux
        .just(CharacterRecord.getMario(), CharacterRecord.getMario(), CharacterRecord.getMario(),
            CharacterRecord.getLuigi(),
            CharacterRecord.getPeach(), CharacterRecord.getMario())
        .distinctUntilChanged(CharacterRecord::prefix, String::equals);

    // THEN
    StepVerifier.create(resultFlux)
        .expectNextSequence(List.of(CharacterRecord.getMario(), CharacterRecord.getPeach(), CharacterRecord.getMario()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithElementAtThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .elementAt(2);

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getPeach())
        .verifyComplete();
  }

  @Test
  void whenTransformWithElementAt2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .elementAt(4, CharacterRecord.getMario());

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithFilterThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filter(CharacterRecord -> CharacterRecord.height() > 1);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getPeach())
        .verifyComplete();
  }

  @Test
  void whenTransformWithFilterWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filterWhen(this::isTall, 1);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .expectNext(CharacterRecord.getLuigi())
        .expectNext(CharacterRecord.getPeach())
        .verifyComplete();
  }

  Mono<Boolean> isTall(CharacterRecord CharacterRecord) {
    return Mono.just(CharacterRecord.height() > 1);
  }

  @Test
  void whenTransformWithFlatMapWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMap(this::mapToName, 2, 3);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(TOAD_NAME)
        .expectNext(PEACH_NAME)
        .verifyComplete();
  }

  @Test
  void whenTransformWithFlatMapDelayErrorWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Boolean> CharacterRecordFlux = this.getValues(List.of())
        .flatMapDelayError(this::isTall, 2, 3);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .verifyError(IllegalArgumentException.class);
  }

  @Test
  void whenTransformWithFlatMapConsumersWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux = this.getValues(SUPER_MARIO_CHARACTERS)
        .switchIfEmpty(Mono.error(Exception::new))
        .flatMap(this::mapToName, throwable -> Mono.just("ERROR"), () -> Flux.just("PETER"));

    final Flux<String> CharacterRecordFluxError = this.getValues(List.of())
        .switchIfEmpty(Mono.error(Exception::new))
        .flatMap(this::mapToName, throwable -> Flux.just("ERROR", "FULLED"), Mono::empty);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(TOAD_NAME)
        .expectNext("PETER")
        .expectNext(PEACH_NAME)
        .verifyComplete();

    StepVerifier.create(CharacterRecordFluxError)
        .expectNext("ERROR")
        .expectNext("FULLED")
        .verifyComplete();
  }

  Flux<CharacterRecord> getValues(List<CharacterRecord> CharacterRecordList) {
    return Flux.fromIterable(CharacterRecordList)
        .switchIfEmpty(Mono.error(IllegalArgumentException::new));
  }

  Mono<String> mapToName(CharacterRecord CharacterRecord) {
    if (CharacterRecord.equals(com.kairosds.webflux.samples.CharacterRecord.getPeach())) {
      return Mono.just(CharacterRecord.name()).delayElement(Duration.ofSeconds(2));
    }
    return Mono.just(CharacterRecord.name());
  }

  @Test
  void whenTransformWithFlatMapIterableWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMapIterable(CharacterRecord -> List.of(CharacterRecord.id(), CharacterRecord.name()), 10);

    // THEN
    StepVerifier.create(CharacterRecordFlux.log())
        .expectNext("1", MARIO_NAME)
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  void whenTransformWithFlatMapSequentialWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMapSequential(this::mapToName);

    // THEN
    StepVerifier.create(CharacterRecordFlux.log())
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(PEACH_NAME)
        .expectNext(TOAD_NAME)
        .verifyComplete();
  }

  @Test
  void whenTransformWithGroupByWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<GroupedFlux<String, String>> CharacterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .groupBy(CharacterRecord::prefix, CharacterRecord::name);

    // THEN
    StepVerifier.create(CharacterRecordFlux.log())
        .assertNext(stringCharacterRecordGroupedFlux -> assertThat(stringCharacterRecordGroupedFlux).extracting("key").isEqualTo("SR"))
        .assertNext(stringCharacterRecordGroupedFlux -> assertThat(stringCharacterRecordGroupedFlux).extracting("key").isEqualTo("MISS"))
        .verifyComplete();
  }
}

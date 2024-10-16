package com.kairosds.webflux.samples.d_transformations;

import static com.kairosds.webflux.samples.CharacterRecord.LUIGI_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.MARIO_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.PEACH_NAME;

import java.util.List;
import java.util.Optional;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Mono1TransformationsTest {

  public static List<String> SUPER_MARIO_NAMES = List.of(MARIO_NAME, LUIGI_NAME, PEACH_NAME);

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenTransformWithAndThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> voidMono = Mono.just(CharacterRecord.getMario())
        .and(Mono.just(CharacterRecord.getPeach()));

    // THEN
    StepVerifier.create(voidMono)
        .verifyComplete();
  }

  @Test
  void whenTransformWithFluxThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> CharacterRecordFlux = Mono.just(CharacterRecord.getMario())
        .flux();

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext(CharacterRecord.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSingleThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<CharacterRecord> CharacterRecordMono = Mono.just(CharacterRecord.getMario())
        .single();

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(CharacterRecord.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSingleOptionalThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Optional<CharacterRecord>> CharacterRecordMono = Mono.just(CharacterRecord.getMario())
        .singleOptional();

    // THEN
    StepVerifier.create(CharacterRecordMono)
        .expectNext(Optional.of(CharacterRecord.getMario()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> voidMono = Mono.when(Flux.fromIterable(SUPER_MARIO_NAMES));

    // THEN
    StepVerifier.create(voidMono)
        .verifyComplete();
  }

  @Test
  void whenTransformWithFromRunnableThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> voidMono = Mono.fromRunnable(() -> this.executeBlockingCode());

    // THEN
    StepVerifier.create(voidMono)
        .verifyComplete();
  }

  @Test
  void whenTransformWithFromSupplierThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<String> stringMono = Mono.fromSupplier(() -> this.executeBlockingSupplier());

    // THEN
    StepVerifier.create(stringMono)
        .expectNext("VALUE")
        .verifyComplete();
  }

  private void executeBlockingCode() {
  }

  private String executeBlockingSupplier() {
    return "VALUE";
  }
}

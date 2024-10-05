package com.kairosds.webflux.d_transformations;

import static com.kairosds.webflux.Character.LUIGI_NAME;
import static com.kairosds.webflux.Character.MARIO_NAME;
import static com.kairosds.webflux.Character.PEACH_NAME;

import java.util.List;
import java.util.Optional;

import com.kairosds.webflux.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Mono1TransformationsTest {

  public static List<String> SUPER_MARIO_NAMES = List.of(MARIO_NAME, LUIGI_NAME, PEACH_NAME);

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenTransformWithAndThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> voidMono = Mono.just(Character.getMario())
        .and(Mono.just(Character.getPeach()));

    // THEN
    StepVerifier.create(voidMono)
        .verifyComplete();
  }

  @Test
  void whenTransformWithFluxThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Mono.just(Character.getMario())
        .flux();

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSingleThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Mono.just(Character.getMario())
        .single();

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithSingleOptionalThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Optional<Character>> characterMono = Mono.just(Character.getMario())
        .singleOptional();

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Optional.of(Character.getMario()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Void> voidMono = Mono.whenDelayError(Flux.fromIterable(SUPER_MARIO_NAMES));

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

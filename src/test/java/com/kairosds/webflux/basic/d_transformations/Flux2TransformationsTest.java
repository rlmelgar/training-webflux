package com.kairosds.webflux.basic.d_transformations;

import static com.kairosds.webflux.domain.model.Character.LUIGI_NAME;
import static com.kairosds.webflux.domain.model.Character.MARIO_NAME;
import static com.kairosds.webflux.domain.model.Character.PEACH_NAME;
import static com.kairosds.webflux.domain.model.Character.TOAD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

import com.kairosds.webflux.domain.model.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Flux2TransformationsTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenTransformWithDistinctThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .distinct(Character::name, HashSet::new, (objects, s) -> objects.add(s.substring(2, 3)),
            charactersHashSetKeys -> charactersHashSetKeys.forEach(object -> System.out.println("Keys:" + object)));

    // THEN
    StepVerifier.create(resultFlux)
        .expectNextSequence(List.of(Character.getMario(), Character.getLuigi(), Character.getPeach()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithDistinctUntilChangedThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> resultFlux = Flux
        .just(Character.getMario(), Character.getMario(), Character.getMario(),
            Character.getLuigi(),
            Character.getPeach(), Character.getMario())
        .distinctUntilChanged(Character::prefix, String::equals);

    // THEN
    StepVerifier.create(resultFlux)
        .expectNextSequence(List.of(Character.getMario(), Character.getPeach(), Character.getMario()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithElementAtThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .elementAt(2);

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getPeach())
        .verifyComplete();
  }

  @Test
  void whenTransformWithElementAt2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .elementAt(4, Character.getMario());

    // THEN
    StepVerifier.create(characterMono)
        .expectNext(Character.getMario())
        .verifyComplete();
  }

  @Test
  void whenTransformWithFilterThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filter(character -> character.height() > 1);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .expectNext(Character.getPeach())
        .verifyComplete();
  }

  @Test
  void whenTransformWithFilterWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .filterWhen(this::isTall, 1);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(Character.getMario())
        .expectNext(Character.getLuigi())
        .expectNext(Character.getPeach())
        .verifyComplete();
  }

  Mono<Boolean> isTall(Character character) {
    return Mono.just(character.height() > 1);
  }

  @Test
  void whenTransformWithFlatMapWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMap(this::mapToName, 2, 3);

    // THEN
    StepVerifier.create(characterFlux)
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
    final Flux<Boolean> characterFlux = this.getValues(List.of())
        .flatMapDelayError(this::isTall, 2, 3);

    // THEN
    StepVerifier.create(characterFlux)
        .verifyError(IllegalArgumentException.class);
  }

  @Test
  void whenTransformWithFlatMapConsumersWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = this.getValues(SUPER_MARIO_CHARACTERS)
        .switchIfEmpty(Mono.error(Exception::new))
        .flatMap(this::mapToName, throwable -> Mono.just("ERROR"), () -> Flux.just("PETER"));

    final Flux<String> characterFluxError = this.getValues(List.of())
        .switchIfEmpty(Mono.error(Exception::new))
        .flatMap(this::mapToName, throwable -> Flux.just("ERROR", "FULLED"), Mono::empty);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext(MARIO_NAME)
        .expectNext(LUIGI_NAME)
        .expectNext(TOAD_NAME)
        .expectNext("PETER")
        .expectNext(PEACH_NAME)
        .verifyComplete();

    StepVerifier.create(characterFluxError)
        .expectNext("ERROR")
        .expectNext("FULLED")
        .verifyComplete();
  }

  Flux<Character> getValues(List<Character> characterList) {
    return Flux.fromIterable(characterList)
        .switchIfEmpty(Mono.error(IllegalArgumentException::new));
  }

  Mono<String> mapToName(Character character) {
    if (character.equals(Character.getPeach())) {
      return Mono.just(character.name()).delayElement(Duration.ofSeconds(2));
    }
    return Mono.just(character.name());
  }

  @Test
  void whenTransformWithFlatMapIterableWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMapIterable(character -> List.of(character.id(), character.name()), 10);

    // THEN
    StepVerifier.create(characterFlux.log())
        .expectNext("1", MARIO_NAME)
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  void whenTransformWithFlatMapSequentialWhenThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMapSequential(this::mapToName);

    // THEN
    StepVerifier.create(characterFlux.log())
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
    final Flux<GroupedFlux<String, String>> characterFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .groupBy(Character::prefix, Character::name);

    // THEN
    StepVerifier.create(characterFlux.log())
        .assertNext(stringCharacterGroupedFlux -> assertThat(stringCharacterGroupedFlux).extracting("key").isEqualTo("SR"))
        .assertNext(stringCharacterGroupedFlux -> assertThat(stringCharacterGroupedFlux).extracting("key").isEqualTo("MISS"))
        .verifyComplete();
  }
}

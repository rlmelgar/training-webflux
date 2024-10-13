package com.kairosds.webflux.basic.d_transformations;

import static com.kairosds.webflux.domain.model.Character.LUIGI_NAME;
import static com.kairosds.webflux.domain.model.Character.MARIO_NAME;
import static com.kairosds.webflux.domain.model.Character.PEACH_NAME;
import static com.kairosds.webflux.domain.model.Character.TOAD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kairosds.webflux.domain.model.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Flux1TransformationsTest {

  Flux1Transformations fluxTrans = new Flux1Transformations();

  public static List<String> SUPER_MARIO_NAMES = List.of(MARIO_NAME, LUIGI_NAME, PEACH_NAME);

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenTransformWithAllThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Character> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS);
    final Mono<Boolean> hasHeightOver0 = resultFlux.all(character -> character.height() > 0);

    // THEN
    StepVerifier.create(hasHeightOver0)
        .expectNext(true)
        .verifyComplete();

    // THEN
    StepVerifier.create(resultFlux)
        .expectNext(Character.getMario())
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  void whenTransformWithAnyThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Boolean> booleanMono = Flux.fromIterable(SUPER_MARIO_NAMES)
        .any("Luigi"::equals);

    // THEN
    StepVerifier.create(booleanMono)
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  void whenTransformWithAsThenReturnIt() {
    // GIVEN

    // WHEN

    // THEN
    Flux.fromIterable(SUPER_MARIO_NAMES)
        .as(StepVerifier::create)
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  void whenTransformWithBufferThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<List<String>> listFlux = Flux.fromIterable(SUPER_MARIO_NAMES)
        .buffer();

    // THEN
    StepVerifier.create(listFlux)
        .expectNext(SUPER_MARIO_NAMES)
        .verifyComplete();
  }

  @Test
  void whenTransformWithBuffer2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<List<String>> listFlux = Flux.fromIterable(SUPER_MARIO_NAMES)
        .buffer(2);

    // THEN
    StepVerifier.create(listFlux)
        .expectNext(List.of(MARIO_NAME, LUIGI_NAME))
        .expectNext(List.of(PEACH_NAME))
        .verifyComplete();
  }

  @Test
  void whenTransformWithBuffer3ThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<List<String>> listFlux = Flux.fromIterable(SUPER_MARIO_NAMES)
        .buffer(2, ArrayList::new);

    // THEN
    StepVerifier.create(listFlux)
        .expectNext(List.of(MARIO_NAME, LUIGI_NAME))
        .expectNext(List.of(PEACH_NAME))
        .verifyComplete();
  }

  @Test
  void whenTransformWithBuffer4ThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<List<String>> listFlux = Flux.fromIterable(SUPER_MARIO_NAMES)
        .buffer(2, 1);

    // THEN
    StepVerifier.create(listFlux)
        .expectNext(List.of(MARIO_NAME, LUIGI_NAME))
        .expectNext(List.of(LUIGI_NAME, PEACH_NAME))
        .expectNext(List.of(PEACH_NAME))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCastThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<Object> objectFlux = Flux.fromIterable(SUPER_MARIO_NAMES)
        .cast(Object.class);

    // THEN
    StepVerifier.create(objectFlux)
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<ArrayList<String>> arrayListMono = Flux.fromIterable(SUPER_MARIO_NAMES)
        .collect(ArrayList::new, ArrayList::add);

    // THEN
    StepVerifier.create(arrayListMono)
        .expectNext(new ArrayList<>(SUPER_MARIO_NAMES))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollect2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<List<String>> listMono = Flux.fromIterable(SUPER_MARIO_NAMES)
        .collect(Collectors.toUnmodifiableList());

    // THEN
    StepVerifier.create(listMono)
        .expectNext(SUPER_MARIO_NAMES)
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectListThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<List<String>> listMono = Flux.fromIterable(SUPER_MARIO_NAMES)
        .collectList();

    // THEN
    StepVerifier.create(listMono)
        .expectNext(SUPER_MARIO_NAMES)
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMapThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, Character>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMap(Character::id);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterMap -> assertThat(stringCharacterMap).hasSize(4)
            .allSatisfy((key, character) -> assertThat(key).isEqualTo(character.id())))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMap2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, String>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMap(Character::id, Character::name);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterMap -> assertThat(stringCharacterMap).hasSize(4)
            .containsKeys("1", "2", "3", "4")
            .containsValues(MARIO_NAME, LUIGI_NAME, PEACH_NAME, TOAD_NAME))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMap3ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, String>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMap(Character::id, Character::name, HashMap::new);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterMap -> assertThat(stringCharacterMap)
            .isOfAnyClassIn(HashMap.class)
            .hasSize(4)
            .containsKeys("1", "2", "3", "4")
            .containsValues(MARIO_NAME, LUIGI_NAME, PEACH_NAME, TOAD_NAME))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMultimapThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, Collection<Character>>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMultimap(Character::prefix);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterMap -> assertThat(stringCharacterMap)
            .hasSize(2)
            .containsKeys("SR", "MISS")
            .containsValues(List.of(Character.getMario(), Character.getLuigi(), Character.getToad()),
                List.of(Character.getPeach())))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMultimap2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, Collection<String>>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMultimap(Character::prefix, Character::name);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterMap -> assertThat(stringCharacterMap)
            .hasSize(2)
            .containsKeys("SR", "MISS")
            .containsValues(List.of(MARIO_NAME, LUIGI_NAME, TOAD_NAME), List.of(PEACH_NAME)))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMultimap3ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, Collection<String>>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMultimap(Character::prefix, Character::name, HashMap::new);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterMap -> assertThat(stringCharacterMap)
            .hasSize(2)
            .containsKeys("SR", "MISS")
            .containsValues(List.of(MARIO_NAME, LUIGI_NAME, TOAD_NAME), List.of(PEACH_NAME)))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectSortedListThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<List<Character>> listMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .sort(Comparator.comparingInt(Character::height))
        .collectSortedList(Character::compareTo);

    // THEN
    StepVerifier.create(listMono)
        .assertNext(characterList -> assertThat(characterList)
            .isOfAnyClassIn(ArrayList.class)
            .containsExactly(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectSortedList2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<List<Character>> listMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .sort(Comparator.comparingInt(Character::height))
        .collectSortedList();

    // THEN
    StepVerifier.create(listMono)
        .assertNext(characterList -> assertThat(characterList)
            .isOfAnyClassIn(ArrayList.class)
            .containsExactly(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCountThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Long> longMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .count();

    // THEN
    StepVerifier.create(longMono)
        .expectNext(4L)
        .verifyComplete();
  }

  @Test
  void whenHasPeachTransformToMapsThenReturnIt() {
    // GIVEN
    final Flux<Character> charactersFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS);

    // WHEN
    final Mono<Map<Object, Collection<Character>>> isPeachMono =
        charactersFlux
            .any((character) -> PEACH_NAME.equals(character.name()))
            .flatMap(isPeach -> this.getMonoDependingOnPeach(charactersFlux, isPeach));

    // THEN
    StepVerifier.create(isPeachMono)
        .assertNext(stringCharacterMap -> assertThat(stringCharacterMap)
            .hasSize(2)
            .containsKeys(PEACH_NAME, "Others")
            .containsValues(List.of(Character.getMario(), Character.getLuigi(), Character.getToad()),
                List.of(Character.getPeach())))
        .verifyComplete();

  }

  private Mono<Map<Object, Collection<Character>>> getMonoDependingOnPeach(Flux<Character> charactersFlux, boolean isPeach) {
    if (isPeach) {
      return charactersFlux.collectMultimap(character -> PEACH_NAME.equals(character.name()) ? PEACH_NAME : "Others");
    }
    return charactersFlux.collectMultimap(Character::height);

  }
}

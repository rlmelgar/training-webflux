package com.kairosds.webflux.samples.d_transformations;

import static com.kairosds.webflux.samples.CharacterRecord.LUIGI_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.MARIO_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.PEACH_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.TOAD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Flux1TransformationsTest {

  Flux1Transformations fluxTrans = new Flux1Transformations();

  public static List<String> SUPER_MARIO_NAMES = List.of(MARIO_NAME, LUIGI_NAME, PEACH_NAME);

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenTransformWithAllThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> resultFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS);
    final Mono<Boolean> hasHeightOver0 = resultFlux.all(CharacterRecord -> CharacterRecord.height() > 0);

    // THEN
    StepVerifier.create(hasHeightOver0)
        .expectNext(true)
        .verifyComplete();

    // THEN
    StepVerifier.create(resultFlux)
        .expectNext(CharacterRecord.getMario())
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
    final Mono<Map<String, CharacterRecord>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMap(CharacterRecord::id);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterRecordMap -> assertThat(stringCharacterRecordMap).hasSize(4)
            .allSatisfy((key, CharacterRecord) -> assertThat(key).isEqualTo(CharacterRecord.id())))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMap2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, String>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMap(CharacterRecord::id, CharacterRecord::name);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterRecordMap -> assertThat(stringCharacterRecordMap).hasSize(4)
            .containsKeys("1", "2", "3", "4")
            .containsValues(MARIO_NAME, LUIGI_NAME, PEACH_NAME, TOAD_NAME))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMap3ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, String>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMap(CharacterRecord::id, CharacterRecord::name, HashMap::new);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterRecordMap -> assertThat(stringCharacterRecordMap)
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
    final Mono<Map<String, Collection<CharacterRecord>>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMultimap(CharacterRecord::prefix);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterRecordMap -> assertThat(stringCharacterRecordMap)
            .hasSize(2)
            .containsKeys("SR", "MISS")
            .containsValues(List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getToad()),
                List.of(CharacterRecord.getPeach())))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectMultimap2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<Map<String, Collection<String>>> mapMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .collectMultimap(CharacterRecord::prefix, CharacterRecord::name);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterRecordMap -> assertThat(stringCharacterRecordMap)
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
        .collectMultimap(CharacterRecord::prefix, CharacterRecord::name, HashMap::new);

    // THEN
    StepVerifier.create(mapMono)
        .assertNext(stringCharacterRecordMap -> assertThat(stringCharacterRecordMap)
            .hasSize(2)
            .containsKeys("SR", "MISS")
            .containsValues(List.of(MARIO_NAME, LUIGI_NAME, TOAD_NAME), List.of(PEACH_NAME)))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectSortedListThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<List<CharacterRecord>> listMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .sort(Comparator.comparingInt(CharacterRecord::height))
        .collectSortedList(CharacterRecord::compareTo);

    // THEN
    StepVerifier.create(listMono)
        .assertNext(CharacterRecordList -> assertThat(CharacterRecordList)
            .isOfAnyClassIn(ArrayList.class)
            .containsExactly(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad()))
        .verifyComplete();
  }

  @Test
  void whenTransformWithCollectSortedList2ThenReturnIt() {
    // GIVEN

    // WHEN
    final Mono<List<CharacterRecord>> listMono = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .sort(Comparator.comparingInt(CharacterRecord::height))
        .collectSortedList();

    // THEN
    StepVerifier.create(listMono)
        .assertNext(CharacterRecordList -> assertThat(CharacterRecordList)
            .isOfAnyClassIn(ArrayList.class)
            .containsExactly(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad()))
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
    final Flux<CharacterRecord> CharacterRecordsFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS);

    // WHEN
    final Mono<Map<Object, Collection<CharacterRecord>>> isPeachMono =
        CharacterRecordsFlux
            .any((CharacterRecord) -> PEACH_NAME.equals(CharacterRecord.name()))
            .flatMap(isPeach -> this.getMonoDependingOnPeach(CharacterRecordsFlux, isPeach));

    // THEN
    StepVerifier.create(isPeachMono)
        .assertNext(stringCharacterRecordMap -> assertThat(stringCharacterRecordMap)
            .hasSize(2)
            .containsKeys(PEACH_NAME, "Others")
            .containsValues(List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getToad()),
                List.of(CharacterRecord.getPeach())))
        .verifyComplete();

  }

  private Mono<Map<Object, Collection<CharacterRecord>>> getMonoDependingOnPeach(Flux<CharacterRecord> CharacterRecordsFlux,
      boolean isPeach) {
    if (isPeach) {
      return CharacterRecordsFlux.collectMultimap(CharacterRecord -> PEACH_NAME.equals(CharacterRecord.name()) ? PEACH_NAME : "Others");
    }
    return CharacterRecordsFlux.collectMultimap(CharacterRecord::height);

  }
}

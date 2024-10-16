package com.kairosds.webflux.samples.f_errors;

import static com.kairosds.webflux.samples.CharacterRecord.LUIGI_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.MARIO_NAME;
import static com.kairosds.webflux.samples.CharacterRecord.PEACH_NAME;

import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ErrorsTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Test
  void whenManageErrorWithOnErrorCompleteThenReturnRest() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux =
        Flux.just(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getBowser(), CharacterRecord.getPeach())
            .map(this::mapToName)
            .onErrorComplete(throwable -> throwable instanceof NullPointerException);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .verifyComplete();
  }

  @Test
  void whenManageErrorWithOnErrorContinueThenReturnRest() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux =
        Flux.just(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getBowser(), CharacterRecord.getPeach())
            .map(this::mapToName)
            .onErrorContinue(throwable -> throwable instanceof NullPointerException,
                (throwable, CharacterRecord) -> System.out.println("Error in CharacterRecord "
                    .concat(((CharacterRecord) CharacterRecord).name())));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .expectNext("MISS ".concat(PEACH_NAME))
        .verifyComplete();
  }

  @Test
  void whenManageErrorWithOnErrorMapThenReturnError() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux =
        Flux.just(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getBowser(), CharacterRecord.getPeach())
            .map(this::mapToName)
            .onErrorMap(IllegalArgumentException::new);

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .expectError(IllegalArgumentException.class)
        .verify();
  }

  @Test
  void whenManageErrorWithOnErrorResumeThenReturnError() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux =
        Flux.just(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getBowser(), CharacterRecord.getPeach())
            .map(this::mapToName)
            .collectList()
            .flatMapIterable(stringFlux -> stringFlux)
            .onErrorResume(throwable -> Flux.just("UNKNOWN", "NAME"));

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext("UNKNOWN")
        .expectNext("NAME")
        .verifyComplete();
  }

  @Test
  void whenManageErrorWithOnErrorReturnThenReturnError() {
    // GIVEN

    // WHEN
    final Flux<String> CharacterRecordFlux =
        Flux.just(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getBowser(), CharacterRecord.getPeach())
            .flatMap(this::mapToNameAsync)
            .onErrorReturn("UNKNOWN");

    // THEN
    StepVerifier.create(CharacterRecordFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .expectNext("UNKNOWN")
        .verifyComplete();
  }

  String mapToName(CharacterRecord CharacterRecord) {
    return CharacterRecord.prefix().concat(" ").concat(CharacterRecord.name());
  }

  Mono<String> mapToNameAsync(CharacterRecord CharacterRecord) {
    return Mono.just(CharacterRecord.prefix().concat(" ").concat(CharacterRecord.name()));
  }
}

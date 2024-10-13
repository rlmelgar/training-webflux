package com.kairosds.webflux.f_errors;

import static com.kairosds.webflux.Character.LUIGI_NAME;
import static com.kairosds.webflux.Character.MARIO_NAME;
import static com.kairosds.webflux.Character.PEACH_NAME;

import java.util.List;

import com.kairosds.webflux.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ErrorsTest {

  public static List<Character> SUPER_MARIO_CHARACTERS =
      List.of(Character.getMario(), Character.getLuigi(), Character.getPeach(), Character.getToad());

  @Test
  void whenManageErrorWithOnErrorCompleteThenReturnRest() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux =
        Flux.just(Character.getMario(), Character.getLuigi(), Character.getBowser(), Character.getPeach())
            .map(this::mapToName)
            .onErrorComplete(throwable -> throwable instanceof NullPointerException);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .verifyComplete();
  }

  @Test
  void whenManageErrorWithOnErrorContinueThenReturnRest() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux =
        Flux.just(Character.getMario(), Character.getLuigi(), Character.getBowser(), Character.getPeach())
            .map(this::mapToName)
            .onErrorContinue(throwable -> throwable instanceof NullPointerException,
                (throwable, character) -> System.out.println("Error in character ".concat(((Character) character).name())));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .expectNext("MISS ".concat(PEACH_NAME))
        .verifyComplete();
  }

  @Test
  void whenManageErrorWithOnErrorMapThenReturnError() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux =
        Flux.just(Character.getMario(), Character.getLuigi(), Character.getBowser(), Character.getPeach())
            .map(this::mapToName)
            .onErrorMap(IllegalArgumentException::new);

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .expectError(IllegalArgumentException.class)
        .verify();
  }

  @Test
  void whenManageErrorWithOnErrorResumeThenReturnError() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux =
        Flux.just(Character.getMario(), Character.getLuigi(), Character.getBowser(), Character.getPeach())
            .map(this::mapToName)
            .onErrorResume(throwable -> Flux.just("UNKNOWN", "NAME"));

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .expectNext("UNKNOWN")
        .expectNext("NAME")
        .verifyComplete();
  }

  @Test
  void whenManageErrorWithOnErrorReturnThenReturnError() {
    // GIVEN

    // WHEN
    final Flux<String> characterFlux =
        Flux.just(Character.getMario(), Character.getLuigi(), Character.getBowser(), Character.getPeach())
            .flatMap(this::mapToNameAsync)
            .onErrorReturn("UNKNOWN");

    // THEN
    StepVerifier.create(characterFlux)
        .expectNext("SR ".concat(MARIO_NAME))
        .expectNext("SR ".concat(LUIGI_NAME))
        .expectNext("UNKNOWN")
        .verifyComplete();
  }

  String mapToName(Character character) {
    return character.prefix().concat(" ").concat(character.name());
  }

  Mono<String> mapToNameAsync(Character character) {
    return Mono.just(character.prefix().concat(" ").concat(character.name()));
  }
}

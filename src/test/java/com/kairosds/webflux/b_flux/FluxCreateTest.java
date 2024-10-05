package com.kairosds.webflux.b_flux;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.kairosds.webflux.Character;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FluxCreateTest {

  Flux1Create flux1Create = new Flux1Create();

  @Test
  void whenReceiveStringThenReturnIt() {
    // GIVEN
    final String stringValue = "String";

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getFromString(stringValue);

    // THEN
    StepVerifier.create(stringFlux.log())
        .expectNextMatches(value -> value.equals("String"))
        .verifyComplete();
  }

  @Test
  void whenReceiveMrsAndPeachThenMrsStartWithSAndAllContainsDot() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getFromList(List.of("Sr.Mario", "Sr.Luigi", "Miss.Peach"));

    // THEN
    StepVerifier.create(stringFlux.log())
        .recordWith(ArrayList::new)
        .expectNext("Sr.Mario")
        .consumeRecordedWith(persons -> assertThat(persons).allMatch(value -> value.startsWith("S")))
        .expectNext("Sr.Luigi")
        .expectNext("Miss.Peach")
        .consumeRecordedWith(persons -> assertThat(persons).allMatch(value -> value.contains(".")))
        .expectComplete()
        .verify();
  }

  @Test
  void whenReceiveMarioThenReturnsMario() {
    // GIVEN

    // WHEN
    final Mono<Character> characterMono = Mono.just(Character.getMario());

    // THEN
    StepVerifier.create(characterMono.log())
        .expectNext(Character.getMario())
        .expectComplete()
        .verify();
  }

  @Test
  void whenReceiveMrsAndPeachThenMrsStartWithSAndAllContainsDot2() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getFromList(List.of("Sr.Mario", "Sr.Luigi", "Miss.Peach"));

    // THEN
    StepVerifier.create(stringFlux.log())
        .expectNextMatches("Sr.Mario"::equals)
        .expectNextSequence(() -> List.of("Sr.Luigi", "Miss.Peach").iterator())
        .expectComplete()
        .verify();
  }

  @Test
  void whenReceiveMrsAndPeachThenMrsStartWithSAndAllContainsDot3() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getFromList(List.of("Sr.Mario", "Sr.Luigi", "Miss.Peach"));

    // THEN
    StepVerifier.create(stringFlux.log("<flux logger>"))
        .thenConsumeWhile(value -> value.startsWith("Sr"), value -> assertThat(value).contains("."))
        .expectNextMatches("Miss.Peach"::equals)
        .expectComplete()
        .verify();
  }

  @Test
  void whenFluxIsEmptyThenIsComplete() {
    // GIVEN

    // WHEN
    final Flux<Integer> integerFlux = this.flux1Create.getEmpty();

    // THEN
    StepVerifier.create(integerFlux.log())
        .expectComplete()
        .verify();
  }

  @Test
  void whenFluxIsEmptyThenIsComplete2() {
    // GIVEN

    // WHEN
    final Flux<Integer> integerFlux = this.flux1Create.getEmpty();

    // THEN
    StepVerifier.create(integerFlux.log())
        .expectComplete()
        .verifyThenAssertThat().tookLessThan(Duration.ofSeconds(1));
  }

  @Test
  void whenReceiveDelayedStringThenReturnItLater() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getFromList(List.of("delayed1", "delayed2", "delayed3"));

    // THEN
    StepVerifier.withVirtualTime(() -> stringFlux.delayElements(Duration.ofSeconds(2)).log(), 1000)
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(4))
        .expectNext("delayed1")
        .expectNext("delayed2")
        .expectNoEvent(Duration.ofSeconds(2))
        .expectNext("delayed3")
        .thenAwait(Duration.ofSeconds(2))
        .expectComplete()
        .verify();
  }

  @Test
  void whenExpectErrorThenReturnsIt() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getError();

    // THEN
    StepVerifier.create(stringFlux)
        .expectError(IOException.class)
        .verify();

    StepVerifier.create(stringFlux)
        .verifyError(IOException.class);
  }

  @Test
  void whenExpectErrorMatchesThenReturnsIt() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getError();

    // THEN
    StepVerifier.create(stringFlux)
        .expectErrorMatches(throwable -> throwable instanceof IOException && throwable.getMessage().equals("error flux"))
        .verify();

    StepVerifier.create(stringFlux)
        .verifyErrorMatches(throwable -> throwable instanceof IOException);
  }

  @Test
  void whenReceiveErrorThenReturnIt() {
    // GIVEN

    // WHEN
    final Flux<String> objectFlux = this.flux1Create.getError();

    // THEN
    StepVerifier.create(objectFlux.log())
        .expectError()
        .verify();
  }

  @Test
  void whenExpectErrorMessageThenReturnsIt() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getError();

    // THEN
    StepVerifier.create(stringFlux)
        .expectErrorMessage("error flux")
        .verify();

    StepVerifier.create(stringFlux)
        .verifyErrorMessage("error flux");
  }

  @Test
  void whenExpectErrorSatisfiesThenReturnsIt() {
    // GIVEN

    // WHEN
    final Flux<String> stringFlux = this.flux1Create.getError();

    // THEN
    StepVerifier.create(stringFlux)
        .consumeErrorWith(throwable -> assertThat(throwable)
            .isInstanceOf(IOException.class)
            .hasMessage("error flux"))
        .verify();

    StepVerifier.create(stringFlux)
        .expectErrorSatisfies(throwable -> assertThat(throwable)
            .isInstanceOf(IOException.class)
            .hasMessage("error flux"))
        .verify();

    StepVerifier.create(stringFlux)
        .verifyErrorSatisfies(throwable -> assertThat(throwable)
            .isInstanceOf(IOException.class)
            .hasMessage("error flux"));
  }
}

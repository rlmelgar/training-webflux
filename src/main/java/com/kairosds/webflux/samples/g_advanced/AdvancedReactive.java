package com.kairosds.webflux.samples.g_advanced;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
public class AdvancedReactive {

  private final AuxiliaryService auxiliaryService;

  public static final List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  public static void main(String[] args) {
    final AdvancedReactive advancedReactive = new AdvancedReactive(new AuxiliaryService());

    advancedReactive.shareTest();
    // advancedReactive.shareNextTest();

    try {
      Thread.sleep(8000);
    } catch (final InterruptedException e) {
      log.debug("[ERROR] InterruptedException! ", e);
    }
  }

  public void shareTest() {
    final Flux<CharacterRecord> characterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .delayElements(Duration.ofSeconds(2))
        .share();

    characterRecordFlux.subscribe(characterRecord -> log.debug("[SUBSCRIBER 1] receive: {}", characterRecord.name()));

    try {
      Thread.sleep(4500);
    } catch (final InterruptedException e) {
      log.debug("[ERROR] InterruptedException! ", e);
    }

    characterRecordFlux.subscribe(characterRecord -> log.debug("[SUBSCRIBER 2] receive: {}", characterRecord.name()));
  }

  public void shareNextTest() {
    final Mono<CharacterRecord> characterRecordFlux = Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .delayElements(Duration.ofSeconds(2))
        .shareNext();

    characterRecordFlux.subscribe(characterRecord -> log.debug("[SUBSCRIBER 1] receive: {}", characterRecord.name()));

    try {
      Thread.sleep(4500);
    } catch (final InterruptedException e) {
      log.debug("[ERROR] InterruptedException! ", e);
    }

    characterRecordFlux.subscribe(characterRecord -> log.debug("[SUBSCRIBER 2] receive: {}", characterRecord.name()));
  }

  public Flux<CharacterRecord> fluxWithDefer() {
    return Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .switchIfEmpty(Flux.defer(this.auxiliaryService::getBowserFlux));
  }

  public Flux<CharacterRecord> fluxWithoutDefer() {
    return Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .switchIfEmpty(this.auxiliaryService.getBowserFlux());
  }

  public Flux<CharacterRecord> fluxParallel() {
    return Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .parallel(4)
        .runOn(Schedulers.parallel())
        .flatMap(characterRecord -> this.auxiliaryService.getFriends(characterRecord).delayElements(Duration.ofSeconds(2)))
        .sequential();
  }

  public Flux<CharacterRecord> fluxNoParallel() {
    return Flux.fromIterable(SUPER_MARIO_CHARACTERS)
        .flatMap(characterRecord -> this.auxiliaryService.getFriends(characterRecord).delayElements(Duration.ofSeconds(2)));
  }

}

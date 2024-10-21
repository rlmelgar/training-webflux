package com.kairosds.webflux.samples.g_advanced;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;

import com.kairosds.webflux.samples.CharacterRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AdvancedReactiveTest {

  public static List<CharacterRecord> SUPER_MARIO_CHARACTERS =
      List.of(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad());

  @Mock
  AuxiliaryService auxiliaryService;

  @InjectMocks
  AdvancedReactive advancedReactive;

  @Test
  void whenDeferFluxThenAuxiliaryFluxIsNotExecuted() {
    // GIVEN

    // WHEN
    final Flux<CharacterRecord> characterRecordFlux = this.advancedReactive.fluxWithDefer();

    StepVerifier.create(characterRecordFlux)
        .expectNextCount(4)
        .verifyComplete();

    verify(this.auxiliaryService, times(0)).getBowserFlux();
  }

  @Test
  void whenNoDeferFluxThenAuxiliaryFluxIsExecuted() {
    // GIVEN
    when(this.auxiliaryService.getBowserFlux()).thenReturn(Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser()));

    // WHEN
    final Flux<CharacterRecord> characterRecordFlux = this.advancedReactive.fluxWithoutDefer();

    StepVerifier.create(characterRecordFlux)
        .expectNextCount(4)
        .verifyComplete();

    verify(this.auxiliaryService, times(1)).getBowserFlux();
  }

  @Test
  void whenExecuteFluxInParallelThenIsMuchFaster() {
    // GIVEN
    when(this.auxiliaryService.getFriends(any(CharacterRecord.class))).thenCallRealMethod();

    // WHEN
    final Flux<CharacterRecord> characterRecordParallelFlux = this.advancedReactive.fluxParallel();

    final Duration parallelDuration = StepVerifier.create(characterRecordParallelFlux)
        .expectNextCount(4*3)
        .verifyComplete();

    final Flux<CharacterRecord> characterRecordFlux = this.advancedReactive.fluxNoParallel();

    final Duration noParallelDuration = StepVerifier.create(characterRecordFlux)
        .expectNextCount(4*3)
        .verifyComplete();

    assertThat(parallelDuration.getNano() > noParallelDuration.getNano()).isTrue();
    log.debug("PARALLEL DURATION -> {}s, {}", parallelDuration.getSeconds(), parallelDuration.getNano());
    log.debug("NO PARALLEL DURATION -> {}s, {}", noParallelDuration.getSeconds(), noParallelDuration.getNano());
  }

}

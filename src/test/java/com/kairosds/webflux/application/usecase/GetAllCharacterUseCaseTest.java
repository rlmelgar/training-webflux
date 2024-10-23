package com.kairosds.webflux.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import com.kairosds.webflux.application.builder.CharacterSMBuilder;
import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.domain.port.CharacterPersistencePort;
import com.kairosds.webflux.domain.port.WorldTimePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@ExtendWith(MockitoExtension.class)
class GetAllCharacterUseCaseTest {

  @Mock
  private CharacterPersistencePort characterR2dbcAdapter;

  @Mock
  private CharacterPersistencePort characterMongoDbAdapter;

  @Mock
  private WorldTimePort worldTimePort;

  @InjectMocks
  private GetAllCharacterUseCase getAllCharacterUseCase;

  @BeforeEach
  public void createUseCase() {
    this.getAllCharacterUseCase = new GetAllCharacterUseCase(this.characterR2dbcAdapter, this.characterMongoDbAdapter, this.worldTimePort);
  }

  @Test
  void whenRetrieveFromR2dbcThenReturnTuple() {
    // GIVEN
    final String worldTime = LocalDateTime.now().toString();
    when(this.worldTimePort.getTime()).thenReturn(Mono.just(worldTime));
    when(this.characterR2dbcAdapter.getAll()).thenReturn(Flux.just(CharacterSMBuilder.build()));

    // WHEN
    final Mono<Tuple2<String, List<CharacterSM>>> tuple2Mono = this.getAllCharacterUseCase.getAllR2dbc();

    // THEN
    StepVerifier.create(tuple2Mono)
        .assertNext(tuple2 -> {
          assertThat(tuple2.getT1()).isEqualTo(worldTime);
          assertThat(tuple2.getT2()).contains(CharacterSMBuilder.build());
        })
        .verifyComplete();

    final InOrder inOrder = Mockito.inOrder(this.worldTimePort, this.characterR2dbcAdapter);
    inOrder.verify(this.worldTimePort, Mockito.times(1)).getTime();
    inOrder.verify(this.characterR2dbcAdapter, Mockito.times(1)).getAll();
  }

  @Test
  void whenRetrieveFromMongodbThenReturnFlux() {
    // GIVEN
    when(this.characterMongoDbAdapter.getAll()).thenReturn(Flux.just(CharacterSMBuilder.build()));

    // WHEN
    final Flux<CharacterSM> characterSMFlux = this.getAllCharacterUseCase.getAllMongoDb();

    // THEN
    StepVerifier.create(characterSMFlux)
        .assertNext(characterSM -> assertThat(characterSM).isEqualTo(CharacterSMBuilder.build()))
        .verifyComplete();

    final InOrder inOrder = Mockito.inOrder(this.worldTimePort, this.characterMongoDbAdapter);
    inOrder.verify(this.worldTimePort, Mockito.times(0)).getTime();
    inOrder.verify(this.characterMongoDbAdapter, Mockito.times(1)).getAll();
  }
}

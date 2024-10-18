package com.kairosds.webflux.application.usecase;

import java.util.List;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.domain.port.CharacterPersistencePort;
import com.kairosds.webflux.domain.port.WorldTimePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllCharacterUseCase {

  private final CharacterPersistencePort characterR2dbcAdapter;

  private final CharacterPersistencePort characterMongoDbAdapter;

  private final WorldTimePort worldTimePort;

  public Flux<CharacterSM> getAllMongoDb() {
    log.debug("[START getAll]");
    return this.characterMongoDbAdapter.getAll()
        .doOnComplete(() -> log.debug("[STOP getAll]"));
  }

  public Mono<Tuple2<String, List<CharacterSM>>> getAllR2dbc() {
    log.debug("[START getAllR2dbc]");
    return this.worldTimePort.getTime()
        .zipWith(this.characterR2dbcAdapter.getAll().collectList())
        .doOnSuccess(tuple2 -> log.debug("[STOP getAllR2dbc]"));
  }
}

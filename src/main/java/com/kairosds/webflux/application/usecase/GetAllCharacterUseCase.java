package com.kairosds.webflux.application.usecase;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.domain.port.CharacterPersistencePort;
import com.kairosds.webflux.domain.port.WorldTimePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllCharacterUseCase {

  private final CharacterPersistencePort characterR2dbcAdapter;

  private final CharacterPersistencePort characterMongoDbAdapter;

  private final WorldTimePort worldTimePort;

  public Flux<CharacterSM> getAll() {
    log.debug("[START getAll]");
    return this.worldTimePort.getTime()
        .thenMany(this.characterR2dbcAdapter.getAll()
            .concatWith(this.characterMongoDbAdapter.getAll()))
        .doOnComplete(() -> log.debug("[STOP getAll]"));
  }
}

package com.kairosds.webflux.application.usecase;

import com.kairosds.webflux.domain.port.CharacterPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteCharacterUseCase {

  private final CharacterPersistencePort characterR2dbcAdapter;

  private final CharacterPersistencePort characterMongoDbAdapter;

  public Mono<Void> deleteById(String id) {
    log.debug("[START deleteById] id {}", id);
    return this.characterMongoDbAdapter.deleteById(id)
        .doOnSuccess(unused -> log.debug("[STOP deleteById] id: {}", id));
  }
}

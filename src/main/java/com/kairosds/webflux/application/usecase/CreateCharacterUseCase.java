package com.kairosds.webflux.application.usecase;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.domain.port.CharacterPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCharacterUseCase {

  private final CharacterPersistencePort characterR2dbcAdapter;

  private final CharacterPersistencePort characterMongoDbAdapter;

  public Mono<CharacterSM> create(CharacterSM character) {
    log.debug("[START create] character {}", character);
    return this.characterR2dbcAdapter.insert(character)
        .flatMap(this.characterMongoDbAdapter::insert)
        .doOnSuccess(characterSM -> log.debug("[STOP create] characterSM: {}", characterSM));
  }
}

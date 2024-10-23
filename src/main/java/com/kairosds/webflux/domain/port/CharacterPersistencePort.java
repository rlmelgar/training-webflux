package com.kairosds.webflux.domain.port;

import com.kairosds.webflux.domain.model.CharacterSM;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CharacterPersistencePort {

  Flux<CharacterSM> getAll();

  Mono<CharacterSM> insert(CharacterSM character);

  Mono<Void> deleteById(String id);
}

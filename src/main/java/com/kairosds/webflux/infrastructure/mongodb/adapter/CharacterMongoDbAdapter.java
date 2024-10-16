package com.kairosds.webflux.infrastructure.mongodb.adapter;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.domain.port.CharacterPersistencePort;
import com.kairosds.webflux.infrastructure.mongodb.mapper.CharacterDocumentMapper;
import com.kairosds.webflux.infrastructure.mongodb.repository.CharacterReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("characterMongoDbAdapter")
@RequiredArgsConstructor
@Slf4j
public class CharacterMongoDbAdapter implements CharacterPersistencePort {

  private final CharacterReactiveMongoRepository characterReactiveMongoRepository;

  private final CharacterDocumentMapper characterDocumentMapper;

  public Flux<CharacterSM> getAll() {
    log.debug("[START getAll]");
    return this.characterReactiveMongoRepository.findAll()
        .map(this.characterDocumentMapper::toModel)
        .doOnComplete(() -> log.debug("[STOP getAll]"));
  }

  public Mono<CharacterSM> insert(CharacterSM character) {
    log.debug("[START insert] character {}", character);
    character.setId(null);
    return Mono.just(character).map(this.characterDocumentMapper::toTable)
        .flatMap(this.characterReactiveMongoRepository::save)
        .map(this.characterDocumentMapper::toModel)
        .doOnSuccess(characterSM -> log.debug("[STOP insert] characterSM inserted {}", characterSM));
  }
}

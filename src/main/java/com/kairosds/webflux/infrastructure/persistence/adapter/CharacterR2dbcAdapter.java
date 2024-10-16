package com.kairosds.webflux.infrastructure.persistence.adapter;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.domain.port.CharacterPersistencePort;
import com.kairosds.webflux.infrastructure.persistence.mapper.CharacterTableMapper;
import com.kairosds.webflux.infrastructure.persistence.repository.CharacterR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("characterR2dbcAdapter")
@RequiredArgsConstructor
@Slf4j
public class CharacterR2dbcAdapter implements CharacterPersistencePort {

  private final CharacterR2dbcRepository characterR2dbcRepository;

  private final CharacterTableMapper characterTableMapper;

  @Override
  public Flux<CharacterSM> getAll() {
    log.debug("[START getAll]");
    return this.characterR2dbcRepository.findAll()
        .map(this.characterTableMapper::toModel)
        .doOnComplete(() -> log.debug("[STOP getAll]"));
  }

  @Override
  public Mono<CharacterSM> insert(CharacterSM character) {
    log.debug("[START insert] character {}", character);
    character.setId(null);
    return Mono.just(character).map(this.characterTableMapper::toTable)
        .flatMap(this.characterR2dbcRepository::save)
        .map(this.characterTableMapper::toModel)
        .doOnSuccess(characterSM -> log.debug("[STOP insert] characterSM inserted {}", characterSM));
  }
}

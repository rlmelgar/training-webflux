package com.kairosds.webflux.infrastructure.rest.server.controller;

import com.kairosds.webflux.application.usecase.CreateCharacterUseCase;
import com.kairosds.webflux.application.usecase.GetAllCharacterUseCase;
import com.kairosds.webflux.infrastructure.rest.server.dto.CharacterDto;
import com.kairosds.webflux.infrastructure.rest.server.mapper.CharacterDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(value = "/character")
@RequiredArgsConstructor
@Slf4j
public class CharacterController {

  private final CreateCharacterUseCase createCharacterUseCase;

  private final GetAllCharacterUseCase getAllCharacterUseCase;

  private final CharacterDtoMapper characterDtoMapper;

  @GetMapping("")
  public Flux<CharacterDto> all() {
    log.debug("[START all]");
    return this.getAllCharacterUseCase.getAll()
        .map(this.characterDtoMapper::toDTO)
        .doOnComplete(() -> log.debug("[STOP all]"));
  }

  @PostMapping("")
  public Mono<CharacterDto> create(@RequestBody Mono<CharacterDto> characterDtoMono) {
    log.debug("[START create]");
    return characterDtoMono.map(this.characterDtoMapper::toModel)
        .flatMap(this.createCharacterUseCase::create)
        .map(this.characterDtoMapper::toDTO)
        .doOnSuccess(characterDto -> log.debug("[STOP create] characterDto {}", characterDto));
  }
}

package com.kairosds.webflux.infrastructure.rest.server.controller;

import java.util.List;

import com.kairosds.webflux.application.usecase.CreateCharacterUseCase;
import com.kairosds.webflux.application.usecase.GetAllCharacterUseCase;
import com.kairosds.webflux.infrastructure.rest.server.dto.CharacterDto;
import com.kairosds.webflux.infrastructure.rest.server.dto.CharacterRequest;
import com.kairosds.webflux.infrastructure.rest.server.dto.CharactersWithTimeResponse;
import com.kairosds.webflux.infrastructure.rest.server.mapper.CharacterDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/mongodb")
  public Mono<ResponseEntity<List<CharacterDto>>> getAllWithResponseEntity() {
    log.debug("[START getAllWithResponseEntity]");
    return this.getAllCharacterUseCase.getAllMongoDb()
        .map(this.characterDtoMapper::toDTO)
        .collectList()
        .map(characterSMS -> ResponseEntity.ok().body(characterSMS))
        .doOnSuccess(response -> log.debug("[STOP getAllWithResponseEntity]"));
  }

  @GetMapping("/r2dbc")
  public Mono<CharactersWithTimeResponse> getAll() {
    log.debug("[START all]");
    return this.getAllCharacterUseCase.getAllR2dbc()
        .flatMap(tuple2 -> Flux.fromIterable(tuple2.getT2())
            .map(this.characterDtoMapper::toDTO)
            .collectList()
            .map(characterDtoList -> CharactersWithTimeResponse.builder()
                .worldTime(tuple2.getT1())
                .characterDtoList(characterDtoList)
                .build()))
        .doOnSuccess(response -> log.debug("[STOP all] finish at {}", response));
  }

  @PostMapping("")
  public Mono<CharacterDto> create(@RequestBody Mono<CharacterRequest> characterDtoMono) {
    log.debug("[START create]");
    return characterDtoMono.map(this.characterDtoMapper::toModel)
        .flatMap(this.createCharacterUseCase::create)
        .map(this.characterDtoMapper::toDTO)
        .doOnSuccess(characterDto -> log.debug("[STOP create] characterDto {}", characterDto));
  }
}

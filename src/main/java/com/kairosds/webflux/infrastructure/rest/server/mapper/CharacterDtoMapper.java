package com.kairosds.webflux.infrastructure.rest.server.mapper;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.infrastructure.rest.server.dto.CharacterDto;
import com.kairosds.webflux.infrastructure.rest.server.dto.CharacterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CharacterDtoMapper {

  CharacterDto toDTO(CharacterSM character);

  CharacterSM toModel(CharacterDto characterDto);

  CharacterSM toModel(CharacterRequest characterDto);

}

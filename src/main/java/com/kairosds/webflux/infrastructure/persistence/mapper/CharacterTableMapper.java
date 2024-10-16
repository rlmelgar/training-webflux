package com.kairosds.webflux.infrastructure.persistence.mapper;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.infrastructure.persistence.table.CharacterTable;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",

    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CharacterTableMapper {

  CharacterTable toTable(CharacterSM character);

  CharacterSM toModel(CharacterTable characterTable);

}

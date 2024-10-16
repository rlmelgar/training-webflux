package com.kairosds.webflux.infrastructure.mongodb.mapper;

import com.kairosds.webflux.domain.model.CharacterSM;
import com.kairosds.webflux.infrastructure.mongodb.document.CharacterDocument;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CharacterDocumentMapper {

  CharacterDocument toTable(CharacterSM character);

  CharacterSM toModel(CharacterDocument characterDocument);

}

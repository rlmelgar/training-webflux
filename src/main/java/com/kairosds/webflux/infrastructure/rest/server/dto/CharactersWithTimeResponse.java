package com.kairosds.webflux.infrastructure.rest.server.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain = true)
public class CharactersWithTimeResponse {

  private List<CharacterDto> characterDtoList;

  private String worldTime;

}

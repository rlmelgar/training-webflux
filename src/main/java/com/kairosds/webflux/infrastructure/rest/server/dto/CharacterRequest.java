package com.kairosds.webflux.infrastructure.rest.server.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CharacterRequest {

  private String prefix;

  private String name;

  private int height;

  private int life;
}

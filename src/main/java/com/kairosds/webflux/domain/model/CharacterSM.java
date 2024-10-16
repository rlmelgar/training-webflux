package com.kairosds.webflux.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class CharacterSM {

  private String id;

  private String prefix;

  private String name;

  private int height;

  private int life;

}

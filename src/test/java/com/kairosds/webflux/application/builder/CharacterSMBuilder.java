package com.kairosds.webflux.application.builder;

import com.kairosds.webflux.domain.model.CharacterSM;

public class CharacterSMBuilder {

  private CharacterSMBuilder() {
  }

  public static CharacterSM build() {
    return CharacterSM.builder()
        .id("1")
        .prefix("Sr")
        .name("Mario")
        .height(2)
        .life(2)
        .build();
  }
}

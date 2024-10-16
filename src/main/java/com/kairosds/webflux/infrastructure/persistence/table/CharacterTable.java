package com.kairosds.webflux.infrastructure.persistence.table;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Character")
@Data
@Accessors(chain = true)
public class CharacterTable {

  @Id
  private Long id;

  private String prefix;

  private String name;

  private int height;

  private int life;
}

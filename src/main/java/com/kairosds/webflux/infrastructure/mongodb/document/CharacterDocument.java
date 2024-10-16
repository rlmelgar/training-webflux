package com.kairosds.webflux.infrastructure.mongodb.document;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Character")
@Data
@Accessors(chain = true)
public class CharacterDocument {

  @Id
  private String id;

  private String prefix;

  private String name;

  private int height;

  private int life;
}

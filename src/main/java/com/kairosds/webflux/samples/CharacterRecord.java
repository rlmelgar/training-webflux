package com.kairosds.webflux.samples;

public record CharacterRecord(
    String id,
    String prefix,
    String name,
    int height,
    int life) implements Comparable<CharacterRecord> {

  public CharacterRecord(
      String id,
      String prefix,
      String name,
      int height) {
    this(id, prefix, name, height, 2);
  }

  public static String MARIO_NAME = "Mario";

  public static String LUIGI_NAME = "Luigi";

  public static String PEACH_NAME = "Peach";

  public static String TOAD_NAME = "Toad";

  public static String BOWSER_NAME = "BOWSER";

  public static CharacterRecord getMario() {
    return new CharacterRecord("1", "SR", MARIO_NAME, 2);
  }

  public static CharacterRecord getLuigi() {
    return new CharacterRecord("2", "SR", LUIGI_NAME, 2);
  }

  public static CharacterRecord getPeach() {
    return new CharacterRecord("3", "MISS", PEACH_NAME, 2);
  }

  public static CharacterRecord getToad() {
    return new CharacterRecord("4", "SR", TOAD_NAME, 1);
  }

  public static CharacterRecord getBowser() {
    return new CharacterRecord("5", null, BOWSER_NAME, 4, 10);
  }

  public static CharacterRecord getBowserB() {
    return new CharacterRecord("5", null, BOWSER_NAME.concat("B"), 4, 10);
  }

  public static CharacterRecord getBowser(int life) {
    return new CharacterRecord("5", null, BOWSER_NAME, 4, life);
  }

  public boolean isDead() {
    return this.life < 1;
  }

  @Override
  public int compareTo(CharacterRecord other) {
    return this.id.compareTo(other.id);
  }
}

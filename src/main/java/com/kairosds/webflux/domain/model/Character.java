package com.kairosds.webflux.domain.model;

public record Character(
    String id,
    String prefix,
    String name,
    int height,
    int life) implements Comparable<Character> {

  public Character(
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

  public static Character getMario() {
    return new Character("1", "SR", MARIO_NAME, 2);
  }

  public static Character getLuigi() {
    return new Character("2", "SR", LUIGI_NAME, 2);
  }

  public static Character getPeach() {
    return new Character("3", "MISS", PEACH_NAME, 2);
  }

  public static Character getToad() {
    return new Character("4", "SR", TOAD_NAME, 1);
  }

  public static Character getBowser() {
    return new Character("5", null, BOWSER_NAME, 4, 10);
  }

  public static Character getBowser(int life) {
    return new Character("5", null, BOWSER_NAME, 4, life);
  }

  public boolean isDead() {
    return this.life < 1;
  }

  @Override
  public int compareTo(Character other) {
    return this.id.compareTo(other.id);
  }
}

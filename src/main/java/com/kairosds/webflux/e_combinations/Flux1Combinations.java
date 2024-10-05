package com.kairosds.webflux.e_combinations;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Flux1Combinations {

  public Mono<Boolean> isMario(String text) {
    return Flux.just(text).all(s -> "Mario".equals(text));
  }

  public Flux<String> getFromArray(String... array) {
    return Flux.fromArray(array);
  }

  public Flux<String> getFromList(List<String> list) {
    return Flux.fromIterable(list);
  }

  public Flux<String> getFromStream(Stream<String> stream) {
    return Flux.fromStream(stream);
  }

  public Flux<Integer> getEmpty() {
    return Flux.empty();
  }

  public Flux<String> getError() {
    return Flux.error(new IOException("error flux"));
  }
}

package com.kairosds.webflux.samples.b_flux;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

public class Flux1Create {

  public static void main(String[] args) {
    final Flux1Create flux1Create = new Flux1Create();

    flux1Create.getFromString("Mario Bros")
        .subscribe(stringVal -> System.out.println("getFromString: " + stringVal));
    flux1Create.getFromArray("Mario", "Luigi")
        .subscribe(stringVal -> System.out.println("getFromArray: " + stringVal));
    flux1Create.getFromList(List.of("Mario", "Luigi", "Toad"))
        .subscribe(stringVal -> System.out.println("getFromList: " + stringVal));
    flux1Create.getFromStream(Stream.of("Mario", "Peach"))
        .subscribe(stringVal -> System.out.println("getFromStream: " + stringVal));
    flux1Create.getEmpty().subscribe(stringVal -> System.out.println("getEmpty: " + stringVal));
    flux1Create.getError().subscribe(stringVal -> System.out.println("getError: " + stringVal),
        throwable -> System.out.println("getError: " + throwable.getMessage()),
        () -> System.out.println("COMPLETED"));

    flux1Create.getEmpty().switchIfEmpty(s -> flux1Create.getFromString("ffffff"))
        .switchIfEmpty(s -> Flux.just("ssss"))
        .subscribe(stringVal -> System.out.println("ULTIMO: " + stringVal));

    try {
      Thread.sleep(10000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  public Flux<String> getFromString(String text) {
    return Flux.just(text);
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

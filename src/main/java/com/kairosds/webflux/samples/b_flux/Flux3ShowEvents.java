package com.kairosds.webflux.samples.b_flux;

import java.util.List;

import reactor.core.publisher.Flux;

public class Flux3ShowEvents {

  public static void main(String[] args) {
    showEvents(List.of("Mario", "Luigi", "Peach"))
        .subscribe(s -> System.out.println("Sus1:" + s));

    try {
      Thread.sleep(10000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static Flux<String> showEvents(List<String> stringList) {
    return Flux.fromIterable(stringList)
        .switchIfEmpty(Flux.error(NullPointerException::new))
        .doOnSubscribe(subscription -> System.out.println("[SUBSCRIBE] P->S " + subscription))
        .doFinally(signal -> System.out.println("[FINALLY] S->P " + signal))
        .doOnNext(string -> System.out.println("[NEXT] P->S " + string))
        .doOnError(NullPointerException.class, throwable -> System.out.println("[ERROR] P->S " + throwable))
        .doOnError(throwable -> "Error numeric".equals(throwable.getMessage()),
            throwable -> System.out.println("[ERROR] P->S " + throwable))
        .doOnError(throwable -> {
          System.out.println("[ERROR2] P->S " + throwable);
        })
        .doOnEach(signal -> System.out.println("[EACH] P->S " + signal))
        .doFirst(() -> System.out.println("[FIRST] S->P"))
        .doOnRequest(requestNumber -> System.out.println("[REQUEST] S->P " + requestNumber))
        .doOnTerminate(() -> System.out.println("[TERMINATE] S->P"))
        .doAfterTerminate(() -> System.out.println("[AFTER TERMINATE] S->P"))
        .doOnCancel(() -> System.out.println("[CANCEL] S->P"))
        .doOnComplete(() -> System.out.println("[COMPLETE] P->S"))
        .doOnDiscard(String.class, string -> System.out.println("[DISCARD] P->S " + string));
  }

}

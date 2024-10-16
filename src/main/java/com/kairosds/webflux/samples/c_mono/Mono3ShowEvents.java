package com.kairosds.webflux.samples.c_mono;

import java.util.logging.Level;

import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

public class Mono3ShowEvents {

  public static void main(String[] args) {
    // execute("Mario Bros");
    execute(null);

    try {
      Thread.sleep(10000);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void execute(String value) {
    final Mono<String> stringMono = showEvents(value);

    stringMono.defaultIfEmpty("Nothing")
        .subscribe(stringValue -> printResult("Final value: ", stringValue));
  }

  private static Mono<String> showEvents(String value) {
    return Mono.justOrEmpty(value)
        // .switchIfEmpty(Mono.error(RuntimeException::new))
        .log("****************LOG*************", Level.WARNING, SignalType.ON_COMPLETE, SignalType.AFTER_TERMINATE)
        .doOnSubscribe(subscription -> System.out.println("[SUBSCRIBE] P->S " + subscription))
        .doOnNext(string -> System.out.println("[NEXT] P->S " + string))
        .doOnError(throwable -> "Error numeric".equals(throwable.getMessage()),
            throwable -> System.out.println("[ERROR] P->S " + throwable))
        .doOnEach(signal -> System.out.println("[EACH] P->S " + signal))
        .doFirst(() -> System.out.println("[FIRST] S->P"))
        .doFinally(signal -> System.out.println("[FINALLY] S->P " + signal))
        .doOnRequest(requestNumber -> System.out.println("[REQUEST] S->P " + requestNumber))
        .doOnTerminate(() -> System.out.println("[TERMINATE] S->P"))
        .doAfterTerminate(() -> System.out.println("[AFTER TERMINATE] S->P"))
        .doOnCancel(() -> System.out.println("[CANCEL] S->P"))
        .doOnSuccess(string -> System.out.println("[SUCCESS] P->S " + string))
        .doOnDiscard(String.class, string -> System.out.println("[DISCARD] P->S " + string));
  }

  private static void printResult(String subscriber, String stringValue) {
    System.out.println();
    System.out.println("*****************");
    System.out.println("********** (" + subscriber + ")Value: " + stringValue);
    System.out.println("*****************");
    System.out.println();
  }

}

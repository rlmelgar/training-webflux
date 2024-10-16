package com.kairosds.webflux.domain.port;

import reactor.core.publisher.Mono;

public interface WorldTimePort {

  Mono<String> getTime();
}

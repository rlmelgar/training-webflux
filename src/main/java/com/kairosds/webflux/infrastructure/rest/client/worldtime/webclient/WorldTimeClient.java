package com.kairosds.webflux.infrastructure.rest.client.worldtime.webclient;

import java.time.Duration;

import com.kairosds.webflux.infrastructure.rest.client.worldtime.dto.WorldTimeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorldTimeClient {

  private final WebClient worldTimeWebClient;

  public Mono<WorldTimeDto> getWorldTime() {
    return this.worldTimeWebClient.get()
        .retrieve()
        .bodyToMono(WorldTimeDto.class)
        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(3)))
        .doOnNext(worldTimeDto -> log.debug("worldTimeDto: {}", worldTimeDto.getDatetime()));

  }
}

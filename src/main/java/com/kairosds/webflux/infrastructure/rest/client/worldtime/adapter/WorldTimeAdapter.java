package com.kairosds.webflux.infrastructure.rest.client.worldtime.adapter;

import com.kairosds.webflux.domain.port.WorldTimePort;
import com.kairosds.webflux.infrastructure.rest.client.worldtime.dto.WorldTimeDto;
import com.kairosds.webflux.infrastructure.rest.client.worldtime.webclient.WorldTimeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorldTimeAdapter implements WorldTimePort {

  private final WorldTimeClient worldTimeClient;

  @Override
  public Mono<String> getTime() {
    log.debug("[START getTime]");
    return this.worldTimeClient.getWorldTime()
        .map(WorldTimeDto::getDatetime)
        .doOnSuccess(time -> log.debug("[STOP getTime] time: {}", time));
  }
}

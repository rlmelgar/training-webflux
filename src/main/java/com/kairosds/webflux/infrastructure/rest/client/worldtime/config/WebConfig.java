package com.kairosds.webflux.infrastructure.rest.client.worldtime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

  @Bean
  public WebClient worldTimeWebClient() {
    return WebClient.builder()
        .baseUrl("http://worldtimeapi.org/api/timezone/Europe/Madrid")
        .defaultCookie("cookie-name", "cookie-value")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}

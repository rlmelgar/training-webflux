package com.kairosds.webflux;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableR2dbcRepositories(basePackages = "com.kairosds.webflux.infrastructure.persistence.repository")
@EnableReactiveMongoRepositories(basePackages = "com.kairosds.webflux.infrastructure.mongodb.repository")
@SpringBootApplication
@OpenAPIDefinition
public class WebfluxTrainingApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebfluxTrainingApplication.class, args);
  }

}

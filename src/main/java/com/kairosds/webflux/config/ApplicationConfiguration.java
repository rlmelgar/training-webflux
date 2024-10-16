package com.kairosds.webflux.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class ApplicationConfiguration {

  @Bean
  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

    final var initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ByteArrayResource(
        ("""
              DROP SEQUENCE IF EXISTS primary_key;
              CREATE SEQUENCE primary_key;
              DROP TABLE IF EXISTS Character;
              CREATE TABLE Character (id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, prefix VARCHAR(100) NULL, name VARCHAR(100) NOT NULL, height INT, life INT);
            """)
            .getBytes())));
    return initializer;
  }
}

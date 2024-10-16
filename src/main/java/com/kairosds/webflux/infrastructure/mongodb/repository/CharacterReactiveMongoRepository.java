package com.kairosds.webflux.infrastructure.mongodb.repository;

import com.kairosds.webflux.infrastructure.mongodb.document.CharacterDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterReactiveMongoRepository extends ReactiveMongoRepository<CharacterDocument, String> {
}

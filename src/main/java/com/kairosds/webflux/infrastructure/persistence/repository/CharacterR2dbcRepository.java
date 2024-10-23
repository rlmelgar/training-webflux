package com.kairosds.webflux.infrastructure.persistence.repository;

import com.kairosds.webflux.infrastructure.persistence.table.CharacterTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterR2dbcRepository extends R2dbcRepository<CharacterTable, String> {
}

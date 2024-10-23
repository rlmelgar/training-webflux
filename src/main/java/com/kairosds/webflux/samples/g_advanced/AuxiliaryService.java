package com.kairosds.webflux.samples.g_advanced;

import com.kairosds.webflux.samples.CharacterRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AuxiliaryService {

  public Flux<CharacterRecord> getBowserFlux() {
    return Flux.just(CharacterRecord.getBowser(), CharacterRecord.getBowser());
  }

  public Flux<CharacterRecord> getFriends(CharacterRecord characterRecord) {
    return Flux.just(CharacterRecord.getMario(), CharacterRecord.getLuigi(), CharacterRecord.getPeach(), CharacterRecord.getToad())
        .filter(characterRecord1 -> !characterRecord1.equals(characterRecord));
  }
}

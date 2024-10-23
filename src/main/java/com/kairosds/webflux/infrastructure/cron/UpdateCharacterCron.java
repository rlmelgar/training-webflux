package com.kairosds.webflux.infrastructure.cron;

import com.kairosds.webflux.application.usecase.CreateCharacterUseCase;
import com.kairosds.webflux.domain.model.CharacterSM;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class UpdateCharacterCron {

  private final CreateCharacterUseCase createCharacterUseCase;

  @Scheduled()
  public void execute() {
    this.createCharacterUseCase.create(CharacterSM.builder().build())
        .subscribe();
  }
}

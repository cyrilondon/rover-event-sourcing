package com.rover.domain.model.service.plateau;

import java.util.concurrent.CompletableFuture;

import com.rover.application.command.dto.plateau.PlateauInitializeCmdDto;

public interface PlateauCommandService {
	
	public CompletableFuture<String> initializePlateau(PlateauInitializeCmdDto plateauInitializeCmdDto);

}

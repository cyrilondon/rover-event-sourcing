package com.rover.domain.command.model.service.plateau;

import java.util.concurrent.CompletableFuture;

import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;

public interface PlateauCommandService {
	
	public CompletableFuture<String> initializePlateau(PlateauInitializeCmd plateauInitializeCmd);
	
	public CompletableFuture<String> desactivatePlateau(PlateauDesactivateCmd plateauDesactivateCmd);

}

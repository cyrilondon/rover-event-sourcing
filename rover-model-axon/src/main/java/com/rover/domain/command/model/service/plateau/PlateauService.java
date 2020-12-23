package com.rover.domain.command.model.service.plateau;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;

public interface PlateauService {
	
	public CompletableFuture<UUID> initializePlateau(PlateauInitializeCmd plateauInitializeCmd);
	
	public CompletableFuture<UUID> desactivatePlateau(PlateauDesactivateCmd plateauDesactivateCmd);

}

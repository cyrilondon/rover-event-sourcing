package com.rover.domain.command.model.service.plateau;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.List;

import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.query.PlateauSummary;

public interface PlateauService {
	
	CompletableFuture<UUID> initializePlateau(PlateauInitializeCmd plateauInitializeCmd);
	
	CompletableFuture<UUID> desactivatePlateau(PlateauDesactivateCmd plateauDesactivateCmd);
	
	List<PlateauSummary> findAll();
	
	

}

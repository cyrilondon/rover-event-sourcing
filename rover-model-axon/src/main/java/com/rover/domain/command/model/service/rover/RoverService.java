package com.rover.domain.command.model.service.rover;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.api.RoverMoveCmd;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;
import com.rover.domain.query.RoverSummary;

public interface RoverService {
	
	public CompletableFuture<RoverIdentifier> initializeRover(RoverInitializeCmd roverInitializeCmd);
	
	public CompletableFuture<RoverIdentifier> moveRover(RoverMoveCmd roverMoveCmd);

	RoverSummary  findRoverById(String roverName, String plateauId);
	
	List<RoverSummary>  findRoversByPlateau(String plateauId);
}

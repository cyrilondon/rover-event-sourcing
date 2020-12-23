package com.rover.domain.command.model.service.rover;

import java.util.concurrent.CompletableFuture;

import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;

public interface RoverService {
	
	public CompletableFuture<RoverIdentifier> initializeRover(RoverInitializeCmd roverInitializeCmd);

}

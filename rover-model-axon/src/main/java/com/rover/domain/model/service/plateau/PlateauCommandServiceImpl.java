package com.rover.domain.model.service.plateau;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import com.rover.application.command.dto.plateau.PlateauInitializeCmdDto;
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;

@Service
public class PlateauCommandServiceImpl implements PlateauCommandService {
	
	private final CommandGateway commandGateway;
	
	 public PlateauCommandServiceImpl(CommandGateway commandGateway) {
	        this.commandGateway = commandGateway;
	    }

	@Override
	public CompletableFuture<String> initializePlateau(PlateauInitializeCmdDto plateauInitializeCmdDto) {
		  return commandGateway.send(new PlateauInitializeCmd(UUID.randomUUID(), plateauInitializeCmdDto.getWidth(), plateauInitializeCmdDto.getHeight()));
	}

	@Override
	public CompletableFuture<String> desactivatePlateau(String plateauUUID) {
		 return commandGateway.send(new PlateauDesactivateCmd(UUID.fromString(plateauUUID)));
	}

}

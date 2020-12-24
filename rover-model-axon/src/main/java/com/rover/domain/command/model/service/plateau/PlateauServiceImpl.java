package com.rover.domain.command.model.service.plateau;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.rover.application.out.port.messaging.CommandPublisher;
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;

@Service
public class PlateauServiceImpl implements PlateauService {
	
	private final CommandPublisher commandPublisher;
	
	 public PlateauServiceImpl(CommandPublisher commandPublisher) {
	        this.commandPublisher = commandPublisher;
	    }

	@Override
	public CompletableFuture<UUID> initializePlateau(PlateauInitializeCmd plateauInitializeCmd) {
		  return commandPublisher.send(plateauInitializeCmd);
	}

	@Override
	public CompletableFuture<UUID> desactivatePlateau(PlateauDesactivateCmd plateauDesactivateCmd) {
		 return commandPublisher.send(plateauDesactivateCmd);
	}

}
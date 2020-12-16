package com.rover.domain.command.model.service.plateau;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.rover.application.out.port.messaging.CommandPublisher;
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;

@Service
public class PlateauCommandServiceImpl implements PlateauCommandService {
	
	private final CommandPublisher commandPublisher;
	
	 public PlateauCommandServiceImpl(CommandPublisher commandPublisher) {
	        this.commandPublisher = commandPublisher;
	    }

	@Override
	public CompletableFuture<String> initializePlateau(PlateauInitializeCmd plateauInitializeCmd) {
		  return commandPublisher.send(plateauInitializeCmd);
	}

	@Override
	public CompletableFuture<String> desactivatePlateau(PlateauDesactivateCmd plateauDesactivateCmd) {
		 return commandPublisher.send(plateauDesactivateCmd);
	}

}

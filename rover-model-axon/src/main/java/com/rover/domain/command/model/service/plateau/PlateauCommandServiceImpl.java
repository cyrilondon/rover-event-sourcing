package com.rover.domain.command.model.service.plateau;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.rover.application.command.dto.plateau.PlateauInitializeCmdDto;
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
	public CompletableFuture<String> initializePlateau(PlateauInitializeCmdDto plateauInitializeCmdDto) {
		  return commandPublisher.send(new PlateauInitializeCmd(UUID.randomUUID(), plateauInitializeCmdDto.getWidth(), plateauInitializeCmdDto.getHeight()));
	}

	@Override
	public CompletableFuture<String> desactivatePlateau(String plateauUUID) {
		 return commandPublisher.send(new PlateauDesactivateCmd(UUID.fromString(plateauUUID)));
	}

}

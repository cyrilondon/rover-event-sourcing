package com.rover.domain.command.model.service.plateau;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.rover.application.out.port.messaging.CommandPublisher;
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryRepository;

@Service
public class PlateauServiceImpl implements PlateauService {
	
	private final CommandPublisher commandPublisher;
	
	private final PlateauSummaryRepository plateauRepository;
	
	 public PlateauServiceImpl(CommandPublisher commandPublisher, PlateauSummaryRepository plateauRepository) {
	        this.commandPublisher = commandPublisher;
	        this.plateauRepository = plateauRepository;
	    }

	@Override
	public CompletableFuture<UUID> initializePlateau(PlateauInitializeCmd plateauInitializeCmd) {
		  return commandPublisher.send(plateauInitializeCmd);
	}

	@Override
	public CompletableFuture<UUID> desactivatePlateau(PlateauDesactivateCmd plateauDesactivateCmd) {
		 return commandPublisher.send(plateauDesactivateCmd);
	}

	@Override
	public List<PlateauSummary> findAll() {
		return plateauRepository.findAll();
	}

}

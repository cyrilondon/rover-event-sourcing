package com.rover.domain.command.model.service.rover;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.rover.application.out.port.messaging.CommandPublisher;
import com.rover.core.util.Utils;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.api.RoverMoveCmd;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;
import com.rover.domain.query.RoverSummary;
import com.rover.domain.query.RoverSummaryRepository;

@Service
public class RoverServiceImpl implements RoverService {

	private final CommandPublisher commandPublisher;

	private final RoverSummaryRepository roverRepository;

	public RoverServiceImpl(CommandPublisher commandPublisher, RoverSummaryRepository roverRepository) {
		this.commandPublisher = commandPublisher;
		this.roverRepository = roverRepository;
	}

	@Override
	public CompletableFuture<RoverIdentifier> initializeRover(RoverInitializeCmd roverInitializeCmd) {
		return commandPublisher.send(roverInitializeCmd);
	}

	@Override
	public CompletableFuture<RoverIdentifier> moveRover(RoverMoveCmd roverMoveCmd) {
		return commandPublisher.send(roverMoveCmd);
	}

	@Override
	public RoverSummary findRoverById(String roverName, String plateauId) {
		return Utils.singleResult(roverRepository.findByIdNameAndIdPlateauId(roverName, plateauId));
	}

	@Override
	public List<RoverSummary> findRoversByPlateau(String plateauId) {
		return roverRepository.findByIdPlateauId(plateauId);
	}

}

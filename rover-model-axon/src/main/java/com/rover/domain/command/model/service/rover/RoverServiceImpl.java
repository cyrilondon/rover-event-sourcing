package com.rover.domain.command.model.service.rover;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.rover.application.out.port.messaging.CommandPublisher;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;

@Service
public class RoverServiceImpl implements RoverService {

	private final CommandPublisher commandPublisher;

	public RoverServiceImpl(CommandPublisher commandPublisher) {
		this.commandPublisher = commandPublisher;
	}

	@Override
	public CompletableFuture<RoverIdentifier> initializeRover(RoverInitializeCmd roverInitializeCmd) {
		return commandPublisher.send(roverInitializeCmd);
	}

}

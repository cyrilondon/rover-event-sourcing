package com.rover.domain.command.model.service.rover;

import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.rover.application.out.port.messaging.CommandPublisher;
import com.rover.core.util.Utils;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.api.RoverMoveCmd;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;
import com.rover.domain.query.RoverSummary;

@Service
public class RoverServiceImpl implements RoverService {

	private final CommandPublisher commandPublisher;
	
	private final EntityManager entityManager;

	public RoverServiceImpl(CommandPublisher commandPublisher, EntityManager entityManager) {
		this.commandPublisher = commandPublisher;
		this.entityManager = entityManager;
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
		TypedQuery<RoverSummary> jpaQuery = entityManager.createNamedQuery("RoverSummary.findByNameAndPlateau",
				RoverSummary.class);
		jpaQuery.setParameter("roverName", roverName);
		jpaQuery.setParameter("plateauId", plateauId);
		return Utils.singleResult(jpaQuery.getResultList());
	}

}

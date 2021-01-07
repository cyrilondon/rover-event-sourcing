package com.rover.application.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.rover.domain.api.PlateauMarkLocationsCmd;
import com.rover.domain.api.RoverInitializedEvt;
import com.rover.domain.api.RoverRemoveCmd;
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates;

@Saga
public class RoverInitializedSaga {
	
	@Autowired
	private transient CommandGateway commandGateway;

	@StartSaga(forceNew = true)
	@SagaEventHandler(associationProperty = "roverName")
	public void handle(RoverInitializedEvt event) {
		String plateauId = event.getId().getPlateauId().toString();
		// associate the Saga with this value, before sending the command
		SagaLifecycle.associateWith("plateauId", plateauId);
		// send the commands
		commandGateway.send(new PlateauMarkLocationsCmd(event.getId().getPlateauId(), event.getPosition(),
				new TwoDimensionalCoordinates.Empty())).exceptionally(ex -> { commandGateway.send(new RoverRemoveCmd(event.getId())); return null;});
	    //https://stackoverflow.com/questions/62088542/how-should-i-run-compensations-rollback-steps-for-a-saga-orchestration-with-axon
		SagaLifecycle.end();
	}

}

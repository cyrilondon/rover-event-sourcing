package com.rover.application.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.rover.domain.api.PlateauMarkLocationsCmd;
import com.rover.domain.api.RoverMovedEvt;
import com.rover.domain.api.RoverRemoveCmd;


/**
 * Each command should be scope/limited to the corresponding aggregate.
 * The rover move should NOT send any command to the plateau.
 * It is a Saga responsibility of the Saga for sending the required commands to the plateau - mark the new position as busy
 * and the old position as free - in case of rover move.
 * Also in case of an already existing Rover on the new plateau position, the Saga sends a command to the rover 
 * for entity deletion.
 *
 */
@Saga
public class RoverMovedSaga {

	@Autowired
	private transient CommandGateway commandGateway;

	@StartSaga(forceNew = true)
	@SagaEventHandler(associationProperty = "roverName")
	public void handle(RoverMovedEvt event) {
		String plateauId = event.getId().getPlateauId().toString();
		// associate the Saga with this value, before sending the command
		SagaLifecycle.associateWith("plateauId", plateauId);
		// send the commands
		commandGateway.send(new PlateauMarkLocationsCmd(event.getId().getPlateauId(), event.getPosition(),
				event.getOldPosition())).exceptionally(ex -> {
					commandGateway.send(new RoverRemoveCmd(event.getId())); return null;});
	    //https://stackoverflow.com/questions/62088542/how-should-i-run-compensations-rollback-steps-for-a-saga-orchestration-with-axon
		SagaLifecycle.end();

	}

}

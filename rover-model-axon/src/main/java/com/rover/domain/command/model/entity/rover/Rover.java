package com.rover.domain.command.model.entity.rover;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.lang.invoke.MethodHandles;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rover.application.context.GameContext;
import com.rover.core.validation.ArgumentCheck;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.api.RoverInitializeEvt;
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates;
import com.rover.domain.command.model.exception.GameExceptionLabels;


@Aggregate
public class Rover {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private final RoverValidator roverValidator = GameContext.getInstance().getRoverValidator();
	
	@AggregateIdentifier
	private RoverIdentifier id;
	
	private Orientation orientation;

	private TwoDimensionalCoordinates position;
	
	public Rover() {
		// Required by Axon
		logger.debug("Empty constructor invoked");
	}

	
	/**
	 * We add this constructor with a name parameter to keep track of a given Rover,
	 * in the case of sending commands from sources/clients other than a file (
	 * where the explore command follows immediately the initialize command). By
	 * example, the command could ask for Rover_X to turn left or turn right well
	 * after Rover_X has been initialized.
	 * 
	 * @param rover name
	 * @param rover coordinates
	 * @param rover orientation
	 */
	public Rover(RoverIdentifier id, TwoDimensionalCoordinates coordinates, Orientation orientation) {
		this.id = ArgumentCheck.preNotNull(id, GameExceptionLabels.MISSING_ROVER_IDENTIFIER);
		this.position = ArgumentCheck.preNotNull(coordinates, GameExceptionLabels.MISSING_ROVER_POSITION);
		this.orientation = ArgumentCheck.preNotNull(orientation, GameExceptionLabels.MISSING_ROVER_ORIENTATION);
	}
	
	@CommandHandler
	public Rover(RoverInitializeCmd cmd) {
		logger.debug("handling {}", cmd);
		// basic validation
		ArgumentCheck.preNotNull(cmd.getId(), GameExceptionLabels.MISSING_ROVER_IDENTIFIER);
		ArgumentCheck.preNotNull(cmd.getPosition(), GameExceptionLabels.MISSING_ROVER_POSITION);
		ArgumentCheck.preNotNull(cmd.getOrientation(), GameExceptionLabels.MISSING_ROVER_ORIENTATION);
		// business validation
		roverValidator.doValidate(cmd);
		// publishing the event
		apply(new RoverInitializeEvt(cmd.getId(), cmd.getPosition(), cmd.getOrientation()));
	}
	
	@EventSourcingHandler
	public void on(RoverInitializeEvt evt) {
		logger.debug("applying {}", evt);
		// setting the id
		this.id = evt.getId();
		this.position = evt.getPosition();
		this.orientation = evt.getOrientation();
		logger.debug("new Rover id: {} with position {} and orientation {}", this.id, this.position, this.orientation);
	}


}

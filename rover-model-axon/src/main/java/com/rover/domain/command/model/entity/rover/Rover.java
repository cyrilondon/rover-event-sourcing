package com.rover.domain.command.model.entity.rover;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.lang.invoke.MethodHandles;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rover.application.context.SpringGameContext;
import com.rover.core.validation.ArgumentCheck;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.api.RoverInitializeEvt;
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates;
import com.rover.domain.command.model.exception.GameExceptionLabels;


@Aggregate
public class Rover {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private final RoverValidator roverValidator;
	
	@AggregateIdentifier
	private RoverIdentifier id;
	
	private Orientation orientation;

	private TwoDimensionalCoordinates position;
	
	public Rover() {
		// Required by Axon
		logger.debug("Empty constructor invoked");
		roverValidator = SpringGameContext.getBean(RoverValidator.class);
		
	}
	
	@CommandHandler
	public Rover(RoverInitializeCmd cmd) {
		logger.debug("handling {}", cmd);
		// we need to inject the validator this way as we dont want to make the Rover model aggregate as a spring component
		roverValidator = SpringGameContext.getBean(RoverValidator.class);
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

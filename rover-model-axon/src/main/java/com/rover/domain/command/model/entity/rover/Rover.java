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
import com.rover.domain.api.RoverInitializedEvt;
import com.rover.domain.api.RoverMoveCmd;
import com.rover.domain.api.RoverMovedEvt;
import com.rover.domain.api.RoverRemoveCmd;
import com.rover.domain.api.RoverRemovedEvt;
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
		// we need to inject the validator this way as we dont want to make the Rover
		// model aggregate as a spring component
		// and the validator contains the EventRepository Spring Data repository
		roverValidator = SpringGameContext.getBean(RoverValidator.class);
		// basic validation
		ArgumentCheck.preNotNull(cmd.getId(), GameExceptionLabels.MISSING_ROVER_IDENTIFIER);
		ArgumentCheck.preNotNull(cmd.getPosition(), GameExceptionLabels.MISSING_ROVER_POSITION);
		ArgumentCheck.preNotNull(cmd.getOrientation(), GameExceptionLabels.MISSING_ROVER_ORIENTATION);
		// business validation
		roverValidator.doValidate(cmd.getId().getPlateauId().toString(), cmd.getPosition().getAbscissa(), cmd.getPosition().getOrdinate());
		// publishing the event
		apply(new RoverInitializedEvt(cmd.getId(), cmd.getPosition(), cmd.getOrientation()));
	}

	@EventSourcingHandler
	public void on(RoverInitializedEvt evt) {
		logger.debug("applying {}", evt);
		// setting the id
		this.id = evt.getId();
		this.position = evt.getPosition();
		this.orientation = evt.getOrientation();
		
		logger.debug("new Rover id: {} with position {} and orientation {}", this.id, this.position, this.orientation);
	}

	@CommandHandler
	public void handle(RoverMoveCmd cmd) {
		logger.debug("handling {}", cmd);
		// basic validation
		ArgumentCheck.preNotNull(cmd.getId(), GameExceptionLabels.MISSING_ROVER_IDENTIFIER);
		ArgumentCheck.preNotNull(cmd.getSteps(), GameExceptionLabels.MISSING_ROVER_POSITION);
		ArgumentCheck.preNotNull(cmd.getOrientation(), GameExceptionLabels.MISSING_ROVER_ORIENTATION);
		// business validation
		TwoDimensionalCoordinates targetPosition = this.position.shiftWithOrientation(cmd.getOrientation(), cmd.getSteps());
		roverValidator.doValidate(cmd.getId().getPlateauId().toString(), targetPosition.getAbscissa(), targetPosition.getOrdinate());
		// publishing the event
		apply(new RoverMovedEvt(cmd.getId(), cmd.getOrientation(), targetPosition, position));
	}

	@EventSourcingHandler
	public void on(RoverMovedEvt evt) {
		logger.debug("applying {}", evt);
		this.orientation = evt.getOrientation();
		this.position = evt.getPosition();
		logger.debug("Rover id {} moved with new position {} and orientation {}", this.id, this.position,
				this.orientation);
	}
	
	@CommandHandler
	public void handle(RoverRemoveCmd cmd) {
		logger.debug("handling {}", cmd);
		// basic validation
		ArgumentCheck.preNotNull(cmd.getId(), GameExceptionLabels.MISSING_ROVER_IDENTIFIER);
		// publishing the event
		apply(new RoverRemovedEvt(cmd.getId()));
	}


	public RoverIdentifier getId() {
		return id;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public TwoDimensionalCoordinates getPosition() {
		return position;
	}

}

package com.rover.domain.command.model.entity.plateau;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rover.application.context.GameContext;
import com.rover.core.validation.ArgumentCheck;
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauDesactivatedEvt;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.api.PlateauInitializedEvt;
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates;
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalSpace;
import com.rover.domain.command.model.entity.dimensions.TwoDimensions;
import com.rover.domain.command.model.exception.GameExceptionLabels;

@Aggregate
public class Plateau implements TwoDimensionalSpace {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@AggregateIdentifier
	private UUID plateauId;

	private TwoDimensionalSpace dimensions;

	private PlateauStatus status;

	private int nbfOfPositionsBusy;

	private PlateauValidator plateauValidator = GameContext.getInstance().getPlateauValidator();

	/**
	 * Matrix to keep track of the occupied locations
	 */
	boolean[][] locations;

	public Plateau() {
		// Required by Axon
		logger.debug("Empty constructor invoked");
	}

	@CommandHandler
	public Plateau(PlateauInitializeCmd cmd) {
		logger.debug("handling {}", cmd);
		// basic validation
		ArgumentCheck.preNotNull(cmd.getId(), GameExceptionLabels.MISSING_PLATEAU_UUID);
		ArgumentCheck.preNotNull(cmd.getWidth(), GameExceptionLabels.MISSING_PLATEAU_DIMENSIONS);
		ArgumentCheck.preNotNull(cmd.getHeight(), GameExceptionLabels.MISSING_PLATEAU_DIMENSIONS);
		// business validation
		plateauValidator.doValidate(cmd);
		// publishing the event
		apply(new PlateauInitializedEvt(cmd.getId(), cmd.getWidth(), cmd.getHeight()));
	}

	@CommandHandler
	public void handle(PlateauDesactivateCmd cmd) {
		logger.debug("handling {}", cmd);
		// basic validation
		// publishing the event
		apply(new PlateauDesactivatedEvt(plateauId));
	}

	@EventSourcingHandler
	public void on(PlateauInitializedEvt evt) {
		logger.debug("applying {}", evt);
		// setting the id
		this.plateauId = evt.getId();
		this.dimensions = new TwoDimensions(new TwoDimensionalCoordinates(evt.getWidth(), evt.getHeight()));
		logger.debug("new Plateau id: {} with width {} and height {}", this.plateauId, this.dimensions.getWidth(),
				this.dimensions.getHeight());
		this.status = PlateauStatus.ACTIVE;
		initializeLocations();
	}

	@EventSourcingHandler
	public void on(PlateauDesactivatedEvt evt) {
		logger.debug("applying {}", evt);
		this.status = PlateauStatus.INACTIVE;
		// remove rovers
		resetLocations();
		logger.debug("Plateau id {} : desactivated with status {}", plateauId, status);
	}

	private void initializeLocations() {
		this.locations = new boolean[getLocationIndexFromDimensions(
				dimensions.getWidth())][getLocationIndexFromDimensions(dimensions.getHeight())];
	}

	private void resetLocations() {
		this.locations = null;
	}

	/**
	 * Mark the position as busy/already set
	 * 
	 * @param coordinates
	 * @return
	 */
	public void setLocationOccupied(TwoDimensionalCoordinates coordinates) {
		locations[coordinates.getAbscissa()][coordinates.getOrdinate()] = true;
	}

	/**
	 * Mark the position as busy/already set
	 * 
	 * @param coordinates
	 * @return
	 */
	public void setLocationFree(TwoDimensionalCoordinates coordinates) {
		locations[coordinates.getAbscissa()][coordinates.getOrdinate()] = false;
	}

	/**
	 * Check if the location is already occupied by a Rover or not
	 * 
	 * @param coordinates
	 * @return
	 */
	public boolean isLocationBusy(TwoDimensionalCoordinates coordinates) {
		return locations[coordinates.getAbscissa()][coordinates.getOrdinate()];
	}

	private int getLocationIndexFromDimensions(int coordinate) {
		return coordinate + 1;
	}

	public int getWidth() {
		return dimensions.getWidth();
	}

	public int getHeight() {
		return dimensions.getHeight();
	}

	public TwoDimensionalSpace getDimensions() {
		return dimensions;
	}

	public PlateauStatus getStatus() {
		return status;
	}

	public void setStatus(PlateauStatus status) {
		this.status = status;
	}

	public int getNbfOfPositionsBusy() {
		return nbfOfPositionsBusy;
	}

	public void setNbfOfPositionsBusy(int nbfOfPositionsBusy) {
		this.nbfOfPositionsBusy = nbfOfPositionsBusy;
	}

}

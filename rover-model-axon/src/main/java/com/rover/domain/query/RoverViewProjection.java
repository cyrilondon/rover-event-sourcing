package com.rover.domain.query;

import java.lang.invoke.MethodHandles;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rover.domain.api.RoverInitializedEvt;
import com.rover.domain.api.RoverMovedEvt;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;

@Component
public class RoverViewProjection {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final RoverSummaryRepository repository;
	
	public RoverViewProjection(RoverSummaryRepository repository) {
		this.repository = repository;
	}
	
	@EventHandler
	public void on(RoverInitializedEvt event) {
		logger.debug("projecting {}", event);

		RoverIdentifier roverId = event.getId();

		PlateauSummary plateau = new PlateauSummary();
		plateau.setId(roverId.getPlateauId().toString());
		/*
		 * Update our read model by inserting the new rover. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		repository.save(new RoverSummary(roverId.getName(), plateau, event.getOrientation().getValue(),
				event.getPosition().getAbscissa(), event.getPosition().getOrdinate()));
	}
	
	@EventHandler
	public void on(RoverMovedEvt event) {
		logger.debug("projecting {}", event);

		RoverIdentifier roverId = event.getId();

		PlateauSummary plateau = new PlateauSummary();
		plateau.setId(roverId.getPlateauId().toString());
		/*
		 * Update our read model by inserting the new rover. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		repository.save(new RoverSummary(roverId.getName(), plateau, event.getOrientation().getValue(),
				event.getPosition().getAbscissa(), event.getPosition().getOrdinate()));
	}

}

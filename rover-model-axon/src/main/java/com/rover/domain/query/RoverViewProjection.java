package com.rover.domain.query;

import java.lang.invoke.MethodHandles;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rover.domain.api.RoverInitializedEvt;
import com.rover.domain.api.RoverMovedEvt;
import com.rover.domain.api.RoverRemovedEvt;
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

		// map model id to projection id (with hibernate annotations)
		RoverId projectionId = mapToProjectedRoverId(roverId);

		/*
		 * Update our read model by inserting the new rover. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		repository.save(new RoverSummary(projectionId, event.getOrientation().getValue(),
				event.getPosition().getAbscissa(), event.getPosition().getOrdinate()));
	}

	@EventHandler
	public void on(RoverMovedEvt event) {
		logger.debug("projecting {}", event);

		RoverIdentifier roverId = event.getId();
		
		RoverId projectionId = mapToProjectedRoverId(roverId);

		/*
		 * Update our read model by inserting the new rover. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		repository.save(new RoverSummary(projectionId, event.getOrientation().getValue(),
				event.getPosition().getAbscissa(), event.getPosition().getOrdinate()));
	}
	

	@EventHandler
	public void on(RoverRemovedEvt event) {
		logger.debug("projecting {}", event);

		RoverIdentifier roverId = event.getId();
		
		RoverId projectionId = mapToProjectedRoverId(roverId);
		
		repository.delete(new RoverSummary(projectionId));
		logger.debug("rover id {} has been removed", event.getId().toString());
	}
	
	private RoverId mapToProjectedRoverId(RoverIdentifier roverId) {
		return new RoverId(roverId.getPlateauId().toString(), roverId.getName());
	}

}

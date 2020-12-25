package com.rover.domain.query;

import java.lang.invoke.MethodHandles;

import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rover.domain.api.RoverInitializeEvt;

@Component
public class RoverViewProjection {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final RoverSummaryRepository repository;
	
	public RoverViewProjection(RoverSummaryRepository repository) {
		this.repository = repository;
	}
	
	@EventHandler
	public void on(RoverInitializeEvt event) {
		logger.debug("projecting {}", event);

		String roverId = event.getId().toString();
		/*
		 * Update our read model by inserting the new rover. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		repository.save(new RoverSummary(roverId, event.getOrientation().getValue(), event.getPosition().getAbscissa(), event.getPosition().getOrdinate()));

	}


}

package com.rover.domain.query;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rover.domain.api.PlateauDesactivatedEvt;
import com.rover.domain.api.PlateauInitializedEvt;
import com.rover.domain.command.model.entity.plateau.PlateauStatus;

@Component
public class PlateauViewProjection {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final PlateauSummaryRepository repository;
	private final QueryUpdateEmitter queryUpdateEmitter;

	public PlateauViewProjection(PlateauSummaryRepository repository, QueryUpdateEmitter queryUpdateEmitter) {
		this.repository = repository;
		this.queryUpdateEmitter = queryUpdateEmitter;
	}

	@EventHandler
	public void on(PlateauInitializedEvt event) {
		logger.debug("projecting {}", event);
		/*
		 * Update our read model by inserting the new plateau. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		repository.save(new PlateauSummary(event.getId().toString(), PlateauStatus.ACTIVE));
	}

	@EventHandler
	public void on(PlateauDesactivatedEvt event) {
		logger.debug("projecting {}", event);
		Optional<PlateauSummary> summary = repository.findById(event.getId().toString());
		// should not happen as the first thing done is to rehydrate the state when the
		// command is recieved
		// if the entity is not found before to publish the event we will get an generic
		// AggregateNotFoundException
		if (summary.isPresent()) {
			summary.get().setStatus(PlateauStatus.INACTIVE);
		}
	}
	
	@QueryHandler
	public PlateauSummary handle(FindPlateauSummaryQuery query) {
		return repository.findById(query.getPlateauId()).orElse(null);
	}
	
	

}

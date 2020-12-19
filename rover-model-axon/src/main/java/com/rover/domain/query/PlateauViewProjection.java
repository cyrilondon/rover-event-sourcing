package com.rover.domain.query;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
class PlateauViewProjection {

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
		
		String plateauId = event.getId().toString();
		/*
		 * Update our read model by inserting the new plateau. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		repository.save(new PlateauSummary(plateauId, PlateauStatus.ACTIVE));
		
		queryUpdateEmitter.emit(FindAllPlateauSummaryQuery.class, query -> true, plateauId);
	}

	@EventHandler
	public void on(PlateauDesactivatedEvt event) {
		logger.debug("projecting {}", event);
		Optional<PlateauSummary> summary = repository.findById(event.getId().toString());
		// before this step the first thing done by Axon is to rehydrate the aggregate
		// state
		// from the event table when the command is received
		// if the entity is not found there before to publish the event we will get an
		// generic
		// AggregateNotFoundException
		if (summary.isPresent()) {
			summary.get().setStatus(PlateauStatus.INACTIVE);
		} else {
			throw new IllegalArgumentException("Plateau with id [" + event.getId() + "] could not be found.");
		}
	}

	@QueryHandler
	public PlateauSummary handle(FindPlateauSummaryQuery query) {
		logger.debug("handling query {}", query);
		return repository.findById(query.getPlateauId()).orElseThrow(() -> new IllegalArgumentException(
				"Plateau with id [" + query.getPlateauId() + "] could not be found."));
	}

	@QueryHandler
	public List<String> handle(FindAllPlateauSummaryQuery query) {
		logger.debug("handling query {}", query);
		return repository.findAll().stream().map(PlateauSummary::getId).collect(Collectors.toList());
	}

}

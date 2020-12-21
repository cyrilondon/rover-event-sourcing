package com.rover.domain.query;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
	private final EntityManager entityManager;
	private final QueryUpdateEmitter queryUpdateEmitter;

	public PlateauViewProjection(EntityManager entityManager, PlateauSummaryRepository repository,
			QueryUpdateEmitter queryUpdateEmitter) {
		this.entityManager = entityManager;
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
		repository.save(new PlateauSummary(plateauId, event.getWidth(), event.getHeight(), PlateauStatus.ACTIVE));

		// only emit this event if the id matches the one specified in the GUI search
		// filter
		queryUpdateEmitter.emit(CountPlateauSummaryQuery.class,
				query -> plateauId.startsWith(query.getFilter().getIdStartsWith()), new PlateauCountChangedUpdate());
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
		PlateauSummary updatedPlateauSummary = null;
		if (summary.isPresent()) {
			updatedPlateauSummary = summary.get();
			updatedPlateauSummary.setStatus(PlateauStatus.INACTIVE);
		} else {
			throw new IllegalArgumentException("Plateau with id [" + event.getId() + "] could not be found.");
		}

		/*
		 * Serve the subscribed queries by emitting an update. This reads as follows: -
		 * to all current subscriptions of type FindAllPlateauSummaryQuery - for which
		 * is true that the id of the Plateau having been desactivated starts with the
		 * idStartWith string in the query's filter - send a message containing the new
		 * state of this plateau summary
		 */
		queryUpdateEmitter.emit(FindAllPlateauSummaryQuery.class, query -> summary.get().getId().startsWith(query.getFilter().getIdStartsWith()), updatedPlateauSummary);
	}

	@QueryHandler
	public PlateauSummary handle(FindPlateauSummaryQuery query) {
		logger.debug("handling query {}", query);
		return repository.findById(query.getPlateauId()).orElseThrow(() -> new IllegalArgumentException(
				"Plateau with id [" + query.getPlateauId() + "] could not be found."));
	}

	@QueryHandler
	public List<PlateauSummary> handle(FindAllPlateauSummaryQuery query) {
		logger.debug("handling {}", query);
		TypedQuery<PlateauSummary> jpaQuery = entityManager.createNamedQuery("PlateauSummary.fetch",
				PlateauSummary.class);
		jpaQuery.setParameter("idStartsWith", query.getFilter().getIdStartsWith());
		jpaQuery.setFirstResult(query.getOffset());
		jpaQuery.setMaxResults(query.getLimit());
		return jpaQuery.getResultList();
	}

	@QueryHandler
	public Integer handle(CountPlateauSummaryQuery query) {
		logger.debug("handling {}", query);
		TypedQuery<Long> jpaQuery = entityManager.createNamedQuery("PlateauSummary.count", Long.class);
		jpaQuery.setParameter("idStartsWith", query.getFilter().getIdStartsWith());
		return jpaQuery.getSingleResult().intValue();
	}

}

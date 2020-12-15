package com.rover.domain.query;

import java.lang.invoke.MethodHandles;

import javax.persistence.EntityManager;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rover.domain.api.PlateauInitializedEvt;

@Component
public class PlateauViewProjection {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final EntityManager entityManager;
	private final QueryUpdateEmitter queryUpdateEmitter;

	public PlateauViewProjection(EntityManager entityManager, QueryUpdateEmitter queryUpdateEmitter) {
		this.entityManager = entityManager;
		this.queryUpdateEmitter = queryUpdateEmitter;
	}

	@EventHandler
	public void on(PlateauInitializedEvt event) {
		logger.debug("projecting {}", event);
		/*
		 * Update our read model by inserting the new plateau. This is done so that
		 * upcoming regular (non-subscription) queries get correct data.
		 */
		entityManager.persist(new PlateauSummary(event.getId().toString()));

	}

}

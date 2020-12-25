package com.rover.domain.api;

import java.util.List;

import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Repository to get the events from Axon JPA event store
 */
public interface DomainEventRepository extends JpaRepository<DomainEventEntry, Long> {
	
	List<DomainEventEntry> findByAggregateIdentifier(String aggregateIdentifier);

}

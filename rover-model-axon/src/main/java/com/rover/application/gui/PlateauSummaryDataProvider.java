package com.rover.application.gui;

import java.io.Closeable;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rover.domain.query.CountPlateauSummaryQuery;
import com.rover.domain.query.FindAllPlateauSummaryQuery;
import com.rover.domain.query.PlateauCountChangedUpdate;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.DataChangeEvent;
import com.vaadin.flow.data.provider.Query;

@Component
public class PlateauSummaryDataProvider extends AbstractBackEndDataProvider<PlateauSummary, Void> implements Closeable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final ExecutorService executorService = Executors.newCachedThreadPool();

	private PlateauSummaryFilter filter = new PlateauSummaryFilter("");

	private final QueryGateway queryGateway;

	// keep track of the subscription queries states
	private SubscriptionQueryResult<List<PlateauSummary>, PlateauSummary> fetchQueryResult;
	private SubscriptionQueryResult<Integer, PlateauCountChangedUpdate> countQueryResult;

	public PlateauSummaryDataProvider(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}

	@Override
	protected synchronized Stream<PlateauSummary> fetchFromBackEnd(Query<PlateauSummary, Void> vaadinQuery) {

		if (fetchQueryResult != null) {
			fetchQueryResult.cancel();
			fetchQueryResult = null;
		}

		FindAllPlateauSummaryQuery findAllPlateauSummaryQuery = new FindAllPlateauSummaryQuery(vaadinQuery.getOffset(),
				vaadinQuery.getLimit(), filter);
		logger.debug("submitting from vaadin {}", findAllPlateauSummaryQuery);

		/*
		 * Submitting our query as a subscription query, specifying both the initially
		 * expected response type (multiple PlateauSummaries) as wel as the expected
		 * type of the updates (single CardSummary object). The result is a
		 * SubscriptionQueryResult which contains a project reactor Mono for the initial
		 * response, and a Flux for the updates.
		 */
		fetchQueryResult = queryGateway.subscriptionQuery(findAllPlateauSummaryQuery,
				ResponseTypes.multipleInstancesOf(PlateauSummary.class),
				ResponseTypes.instanceOf(PlateauSummary.class));

		/*
		 * Subscribing to the updates before we get the initial results.
		 */
		fetchQueryResult.updates().subscribe(plateauSummary -> {
			logger.debug("processing query update for {}: {}", findAllPlateauSummaryQuery, plateauSummary);
			/*
			 * This is a Vaadin-specific call to update the UI as a result of data changes.
			 */
			fireEvent(new DataChangeEvent.DataRefreshEvent<>(this, plateauSummary));
		});
		/*
		 * Returning the initial result.
		 */
		return fetchQueryResult.initialResult().block().stream();
	}

	@Override
	protected synchronized int sizeInBackEnd(Query<PlateauSummary, Void> query) {

		if (countQueryResult != null) {
			countQueryResult.cancel();
			countQueryResult = null;
		}

		CountPlateauSummaryQuery countPlateauSummaryQuery = new CountPlateauSummaryQuery(filter);
		logger.debug("submitting from vaadin {}", countPlateauSummaryQuery);

		countQueryResult = queryGateway.subscriptionQuery(countPlateauSummaryQuery,
				ResponseTypes.instanceOf(Integer.class), ResponseTypes.instanceOf(PlateauCountChangedUpdate.class));

		// buffer the update every 250 ms
		countQueryResult.updates().buffer(Duration.ofMillis(250)).subscribe(plateauCountChangedUpdate -> {
			logger.debug("processing query update for {}: {}", countPlateauSummaryQuery, plateauCountChangedUpdate);
			executorService.execute(() -> refreshAll());
			
		});

		/*
		 * Returning the initial result.
		 */
		return countQueryResult.initialResult().block();
	}

	public PlateauSummaryFilter getFilter() {
		return filter;
	}

	public void setFilter(PlateauSummaryFilter filter) {
		this.filter = filter;
	}

	@Override
	public synchronized void close() {
		if (fetchQueryResult != null) {
			fetchQueryResult.cancel();
			fetchQueryResult = null;
		}

		if (countQueryResult != null) {
			countQueryResult.cancel();
			countQueryResult = null;
		}
	}

}

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
import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.Query;

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

		fetchQueryResult = queryGateway.subscriptionQuery(findAllPlateauSummaryQuery,
				ResponseTypes.multipleInstancesOf(PlateauSummary.class),
				ResponseTypes.instanceOf(PlateauSummary.class));

		fetchQueryResult.updates()
				.subscribe(plateauSummary -> fireEvent(new DataChangeEvent.DataRefreshEvent<>(this, plateauSummary)));

		return fetchQueryResult.initialResult().block().stream();
	}

	@Override
	protected synchronized int sizeInBackEnd(Query<PlateauSummary, Void> query) {

		if (countQueryResult != null) {
			countQueryResult.cancel();
			countQueryResult = null;
		}

		CountPlateauSummaryQuery countCardSummaryQuery = new CountPlateauSummaryQuery(filter);
		logger.debug("submitting from vaadin {}", countCardSummaryQuery);

		countQueryResult = queryGateway.subscriptionQuery(countCardSummaryQuery,
				ResponseTypes.instanceOf(Integer.class), ResponseTypes.instanceOf(PlateauCountChangedUpdate.class));

		// buffer the update every 250 ms
		countQueryResult.updates().buffer(Duration.ofMillis(250)).subscribe(plateauCountChangedUpdate -> executorService.execute(() -> fireEvent(new DataChangeEvent<>(this))));

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

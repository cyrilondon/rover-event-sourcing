package com.rover.application.gui;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rover.domain.query.CountPlateauSummaryQuery;
import com.rover.domain.query.FindAllPlateauSummaryQuery;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

@Component
public class PlateauSummaryDataProvider extends AbstractBackEndDataProvider<PlateauSummary, Void> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private PlateauSummaryFilter filter = new PlateauSummaryFilter("");

	private final QueryGateway queryGateway;

	public PlateauSummaryDataProvider(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}

	@Override
	protected Stream fetchFromBackEnd(Query vaadinQuery) {
		FindAllPlateauSummaryQuery findAllPlateauSummaryQuery = new FindAllPlateauSummaryQuery(vaadinQuery.getOffset(), vaadinQuery.getLimit(), filter);
		logger.debug("submitting from vaadin {}", findAllPlateauSummaryQuery);
		return queryGateway.query(findAllPlateauSummaryQuery, ResponseTypes.multipleInstancesOf(PlateauSummary.class))
				.join().stream();
	}

	@Override
	protected int sizeInBackEnd(Query query) {
		CountPlateauSummaryQuery countCardSummaryQuery = new CountPlateauSummaryQuery(filter);
		logger.debug("submitting from vaadin {}", countCardSummaryQuery);
		return queryGateway.query(countCardSummaryQuery, ResponseTypes.instanceOf(Integer.class)).join();
	}

	public PlateauSummaryFilter getFilter() {
		return filter;
	}

	public void setFilter(PlateauSummaryFilter filter) {
		this.filter = filter;
	}

}

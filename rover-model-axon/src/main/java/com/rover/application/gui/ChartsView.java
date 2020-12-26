package com.rover.application.gui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.FillBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.xaxis.Title;
import com.github.appreciated.apexcharts.helper.Series;
import com.rover.domain.query.FindAllPlateauWithRoverSummaryQuery;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
import com.rover.domain.query.RoverSummary;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "charts", layout = MainLayout.class)
@PageTitle("NASA Charts")
public class ChartsView extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	private final QueryGateway queryGateway;

	List<PlateauSummary> plateauList = new ArrayList<>();

	public ChartsView(QueryGateway queryGateway) throws Exception {
		this.queryGateway = queryGateway;
		setHeightFull();
		setId("charts-view");
		plateauList = findAll().get();
		chartPlateauDiv().forEach(this::add);
	}

	private CompletableFuture<List<PlateauSummary>> findAll() {
		return queryGateway.query(new FindAllPlateauWithRoverSummaryQuery(0, 20, new PlateauSummaryFilter("")),
				ResponseTypes.multipleInstancesOf(PlateauSummary.class));
	}

	private List<Div> chartPlateauDiv() {
		List<Div> result = new ArrayList<Div>();
		if (!plateauList.isEmpty()) {
			result = plateauList.stream().map(plateau -> buildPlateauDiv(plateau)).collect(Collectors.toList());
		}
		return result;
	}

	private Div buildPlateauDiv(PlateauSummary plateau) {
		
		Title xtitle = new Title();
		xtitle.setText("plateau width " + plateau.getWidth());
		
		com.github.appreciated.apexcharts.config.yaxis.Title ytitle = new com.github.appreciated.apexcharts.config.yaxis.Title();
		ytitle.setText("plateau height " + plateau.getHeight());
		
		Series[] series = new Series[] {};
		if (!plateau.getRovers().isEmpty()) {
			series = plateau.getRovers().stream().map(rover -> toSeries(rover)).toArray(Series[]::new);
		}

		ApexCharts bubbleChart = ApexChartsBuilder.get()
				.withChart(ChartBuilder.get().withType(Type.bubble)
						.withZoom(ZoomBuilder.get().withEnabled(false).build()).withForeColor("hsl(214,100%,70%").build())
				.withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
				.withFill(FillBuilder.get().withOpacity(0.8).build())
				.withTitle(TitleSubtitleBuilder.get().withText(String.format("Plateau %s Summary Chart ", plateau.getId())).build())
				.withSeries(series)
				.withXaxis(XAxisBuilder.get().withMin(0.0).withMax(Double.valueOf(plateau.getWidth()))
						.withTickAmount(new BigDecimal(plateau.getWidth()))
						.withTitle(xtitle)
						.build())
				.withYaxis(YAxisBuilder.get().withMin(0.0).withMax(Double.valueOf(plateau.getHeight()))
						.withTickAmount(Double.valueOf(plateau.getHeight()))
						.withTitle(ytitle).build())
				.build();
		
		Div chartDiv = new Div();
		chartDiv.add(bubbleChart);
		chartDiv.setMaxWidth("40%");
		return chartDiv;
	}
	
	private Series toSeries(RoverSummary rover) {
		return new Series<Double[]>(rover.getRoverName(), new Double[] {Double.valueOf(rover.getAbscissa()), Double.valueOf(rover.getOrdinate()), 30.0},
				new Double[] {Double.valueOf(rover.getAbscissa()), Double.valueOf(rover.getOrdinate()), 0.0});
	}

}

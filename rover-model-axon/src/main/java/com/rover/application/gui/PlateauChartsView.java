package com.rover.application.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.xaxis.XAxisType;
import com.github.appreciated.apexcharts.helper.Series;
import com.rover.domain.query.FindAllPlateauSummaryQuery;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "plateau_charts", layout = MainLayout.class)
@PageTitle("Plateau Charts")
public class PlateauChartsView extends HorizontalLayout {

	private final QueryGateway queryGateway;

	List<PlateauSummary> plateauList = new ArrayList<>();

	public PlateauChartsView(QueryGateway queryGateway) throws Exception {
		this.queryGateway = queryGateway;
		setHeightFull();
		setId("plateau-charts-view");
		plateauList = findAll().get();
		chartPlateauDiv().forEach(this::add);
	}

	private CompletableFuture<List<PlateauSummary>> findAll() {
		return queryGateway.query(new FindAllPlateauSummaryQuery(0, 20, new PlateauSummaryFilter("")),
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
		Div chartDiv = new Div();
		int width = plateau.getWidth();
		int height = plateau.getHeight();
		String[] labels = IntStream.range(1, width+1).boxed().map(Object::toString).toArray(String[]::new);
		Integer[] series = IntStream.range(1, width+1).boxed().map(e -> height).toArray(Integer[]::new);
		ApexCharts areaChart = ApexChartsBuilder.get()
				.withChart(ChartBuilder.get().withType(Type.area).withZoom(ZoomBuilder.get().withEnabled(false).build())
						.build())
				.withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
				.withStroke(StrokeBuilder.get().withCurve(Curve.straight).build()).withSeries(new Series<>(series))
				.withTitle(TitleSubtitleBuilder.get().withText("Plateau Analysis").withAlign(Align.left).build())
				.withSubtitle(TitleSubtitleBuilder.get().withText("Plateau with id " + plateau.getId())
						.withAlign(Align.left).build())
				.withLabels(labels).withXaxis(XAxisBuilder.get().withType(XAxisType.categories).build())
				.withYaxis(YAxisBuilder.get().withOpposite(true).build())
				.withLegend(LegendBuilder.get().withHorizontalAlign(HorizontalAlign.left).build()).build();

		chartDiv.add(areaChart);
		chartDiv.setMaxWidth("40%");
		return chartDiv;
	}

}

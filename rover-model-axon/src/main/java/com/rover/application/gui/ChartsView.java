package com.rover.application.gui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.rover.application.gui.broadcaster.BroadCaster;
import com.rover.core.util.SerializeUtils;
import com.rover.domain.command.model.entity.rover.RoverInitializedBroadCastEventDto;
import com.rover.domain.query.FindAllPlateauWithRoverSummaryQuery;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
import com.rover.domain.query.RoverSummary;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import com.rover.core.util.Utils;

@Route(value = "charts", layout = MainLayout.class)
@PageTitle("NASA Charts")
public class ChartsView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final String WIDTH_PLATEAU = "50%";

	private static final int PLATEAU_PER_ROW = 2;

	private final QueryGateway queryGateway;

	List<PlateauSummary> plateauList = new ArrayList<>();

	Registration broadcasterRegistration;

	Map<String, ApexCharts> plateauToChart = new HashMap<>();
	
	Map<String, List<Series<Double[]>>> plateauToSeries = new HashMap<>();

	public ChartsView(QueryGateway queryGateway) throws Exception {
		this.queryGateway = queryGateway;
		setId("charts-view");
		plateauList = findAll().get();
		
		List<List<PlateauSummary>> plateau2list = Utils.nPartition(plateauList, PLATEAU_PER_ROW);
		plateau2list.stream().forEach(list -> {
			HorizontalLayout horizontal = new HorizontalLayout();
			horizontal.setSizeFull();
			list.stream().forEach(plateau -> horizontal.add(buildPlateauDiv(plateau)));
			add(horizontal);
		});
		
		setSizeFull();
	}

	private CompletableFuture<List<PlateauSummary>> findAll() {
		return queryGateway.query(new FindAllPlateauWithRoverSummaryQuery(0, 20, new PlateauSummaryFilter("")),
				ResponseTypes.multipleInstancesOf(PlateauSummary.class));
	}

	private Div buildPlateauDiv(PlateauSummary plateau) {

		Title xtitle = new Title();
		xtitle.setText("plateau width " + plateau.getWidth());

		com.github.appreciated.apexcharts.config.yaxis.Title ytitle = new com.github.appreciated.apexcharts.config.yaxis.Title();
		ytitle.setText("plateau height " + plateau.getHeight());

		List<Series<Double[]>> series = new ArrayList<Series<Double[]>>();
		if (!plateau.getRovers().isEmpty()) {
			series = plateau.getRovers().stream().map(rover -> toSeries(rover)).collect(Collectors.toList());
		}

		ApexCharts bubbleChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.bubble)
				.withZoom(ZoomBuilder.get().withEnabled(false).build()).withForeColor("hsl(214,100%,70%").build())
				.withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
				.withFill(FillBuilder.get().withOpacity(0.8).build())
				.withTitle(TitleSubtitleBuilder.get()
						.withText(String.format("Plateau %s Summary Chart ", plateau.getId())).build())
				.withSeries(series.toArray(new Series[series.size()]))
				.withXaxis(XAxisBuilder.get().withMin(0.0).withMax(Double.valueOf(plateau.getWidth()))
						.withTickAmount(new BigDecimal(plateau.getWidth())).withTitle(xtitle).build())
				.withYaxis(YAxisBuilder.get().withMin(0.0).withMax(Double.valueOf(plateau.getHeight()))
						.withTickAmount(Double.valueOf(plateau.getHeight())).withTitle(ytitle).build())
				.build();

		plateauToChart.putIfAbsent(plateau.getId(), bubbleChart);
		plateauToSeries.putIfAbsent(plateau.getId(), series);

		Div chartDiv = new Div();
		chartDiv.add(bubbleChart);
		chartDiv.setWidth(WIDTH_PLATEAU);
		return chartDiv;
	}

	private Series<Double[]> toSeries(RoverSummary rover) {
		return new Series<Double[]>(rover.getRoverName(),
				new Double[] { Double.valueOf(rover.getAbscissa()), Double.valueOf(rover.getOrdinate()), 30.0 },
				new Double[] { Double.valueOf(rover.getAbscissa()), Double.valueOf(rover.getOrdinate()), 0.0 });
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		UI ui = attachEvent.getUI();
		broadcasterRegistration = BroadCaster.register(newMessage -> {
			ui.access(() -> {
				RoverInitializedBroadCastEventDto dto = SerializeUtils.readFromBroadCast(newMessage);
				ApexCharts bubbleChart = plateauToChart.get(dto.getPlateauId());
				List<Series<Double[]>> series = plateauToSeries.get(dto.getPlateauId());
				
				series.add(new Series<Double[]>(dto.getName(),
						new Double[] { Double.valueOf(dto.getAbscissa()), Double.valueOf(dto.getOrdinate()), 30.0 },
						new Double[] { Double.valueOf(dto.getAbscissa()), Double.valueOf(dto.getOrdinate()), 0.0 }));

				bubbleChart.updateSeries(series.toArray(new Series[series.size()]));
				Notification.show(String.format("A rover has just been added %s", dto), 3000, Position.TOP_CENTER);
			});
		});
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		broadcasterRegistration.remove();
		broadcasterRegistration = null;
	}

}

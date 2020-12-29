package com.rover.application.gui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
import com.rover.core.util.Utils;
import com.rover.domain.command.model.entity.rover.RoverInitializedBroadCastEventDto;
import com.rover.domain.command.model.service.plateau.PlateauService;
import com.rover.domain.command.model.service.rover.RoverService;
import com.rover.domain.query.PlateauSummary;
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

@Route(value = "charts", layout = MainLayout.class)
@PageTitle("NASA Charts")
public class ChartsView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final String WIDTH_PLATEAU = "50%";

	private static final int PLATEAU_PER_ROW = 2;

	private final PlateauService plateauService;

	private final RoverService roverService;

	private List<PlateauSummary> plateauList = new ArrayList<>();

	Registration broadcasterRegistration;

	private final Map<String, ApexCharts> plateauToChart = new HashMap<>();

	private final Map<String, Map<String, Series<Double[]>>> plateauToSeries = new HashMap<>();

	public ChartsView(PlateauService plateauService, RoverService roverService)
			throws Exception {
		this.roverService = roverService;
		this.plateauService = plateauService;
		setId("charts-view");
		buildPlateaus();
		setSizeFull();
	}

	private void buildPlateaus() throws InterruptedException, ExecutionException {

		plateauList = plateauService.findAll();

		List<List<PlateauSummary>> plateau2list = Utils.nPartition(plateauList, PLATEAU_PER_ROW);
		plateau2list.stream().forEach(list -> {
			HorizontalLayout horizontal = new HorizontalLayout();
			horizontal.setSizeFull();
			list.stream().forEach(plateau -> horizontal.add(buildPlateauDiv(plateau)));
			add(horizontal);
		});
	}

	private Div buildPlateauDiv(PlateauSummary plateau) {

		Map<String, Series<Double[]>> roverSeries = new TreeMap<>();

		List<RoverSummary> roversInPlateau = roverService.findRoversByPlateau(plateau.getId());
		if (!roversInPlateau.isEmpty()) {
			roverSeries.putAll(roversInPlateau.stream()
					.collect(Collectors.toMap(RoverSummary::getName, rover -> toSeries(rover))));
		}

		ApexCharts bubbleChart = buildBubbleChart(plateau, roverSeries);

		plateauToChart.put(plateau.getId(), bubbleChart);
		plateauToSeries.put(plateau.getId(), roverSeries);

		return addBubbleChartToDiv(bubbleChart);
	}

	private Div addBubbleChartToDiv(ApexCharts bubbleChart) {
		Div chartDiv = new Div();
		chartDiv.add(bubbleChart);
		chartDiv.setWidth(WIDTH_PLATEAU);
		return chartDiv;
	}

	private ApexCharts buildBubbleChart(PlateauSummary plateau, Map<String, Series<Double[]>> roverSeries) {
		Title xtitle = new Title();
		xtitle.setText("plateau width " + plateau.getWidth());

		com.github.appreciated.apexcharts.config.yaxis.Title ytitle = new com.github.appreciated.apexcharts.config.yaxis.Title();
		ytitle.setText("plateau height " + plateau.getHeight());

		ApexCharts bubbleChart = ApexChartsBuilder.get().withChart(ChartBuilder.get().withType(Type.bubble)
				.withZoom(ZoomBuilder.get().withEnabled(false).build()).withForeColor("hsl(214,100%,70%").build())
				.withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
				.withFill(FillBuilder.get().withOpacity(0.8).build())
				.withTitle(TitleSubtitleBuilder.get()
						.withText(String.format("Plateau %s Summary Chart ", plateau.getId())).build())
				.withSeries(roverSeries.values().toArray(new Series[roverSeries.size()]))
				.withXaxis(XAxisBuilder.get().withMin(0.0).withMax(Double.valueOf(plateau.getWidth()))
						.withTickAmount(new BigDecimal(plateau.getWidth())).withTitle(xtitle).build())
				.withYaxis(YAxisBuilder.get().withMin(0.0).withMax(Double.valueOf(plateau.getHeight()))
						.withTickAmount(Double.valueOf(plateau.getHeight())).withTitle(ytitle).build())
				.build();
		return bubbleChart;
	}

	private Series<Double[]> toSeries(RoverSummary rover) {
		return new Series<Double[]>(rover.getName(),
				new Double[] { Double.valueOf(rover.getAbscissa()), Double.valueOf(rover.getOrdinate()), 30.0 },
				new Double[] { Double.valueOf(rover.getAbscissa()), Double.valueOf(rover.getOrdinate()), 0.0 });
	}
	
	private Series<Double[]> toSeries(RoverInitializedBroadCastEventDto broadCastMsgDto) {
		return new Series<Double[]>(broadCastMsgDto.getName(),
				new Double[] { Double.valueOf(broadCastMsgDto.getAbscissa()), Double.valueOf(broadCastMsgDto.getOrdinate()), 30.0 },
				new Double[] { Double.valueOf(broadCastMsgDto.getAbscissa()), Double.valueOf(broadCastMsgDto.getOrdinate()), 0.0 });
	}
	
	
	/**
	 * attach an event listener for message broadcasting when adding/moving a rover
	 */
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		UI ui = attachEvent.getUI();
		broadcasterRegistration = BroadCaster.register(newMessage -> {
			ui.access(() -> {

				try {
					// needed to make sure the last event has been persisted
					// as the broadcast msg is sent once the command has been sent (and not the
					// event persisted)
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (newMessage.contains("Move")) {
					RoverInitializedBroadCastEventDto dto = SerializeUtils.readFromBroadCast(newMessage);
					RoverSummary roverMoved = roverService.findRoverById(dto.getName(), dto.getPlateauId());

					Series<Double[]> seriesToAdd = toSeries(roverMoved);

					// add rover series to current state
					String plateauId = roverMoved.getPlateauId();
					plateauToSeries.putIfAbsent(plateauId, new TreeMap<>());
					Map<String, Series<Double[]>> roverToSeries = plateauToSeries.get(plateauId);
					roverToSeries.put(roverMoved.getName(), seriesToAdd);

					// update bubbleChart with current state and sorted by roverName
					List<Series<Double[]>> series = roverToSeries.values().stream()
							.sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
							.collect(Collectors.toList());
					ApexCharts bubbleChart = plateauToChart.get(plateauId);
					bubbleChart.updateSeries(series.toArray(new Series[series.size()]));

					// show notification of new bubble
					Notification.show(String.format("A rover has just been moved %s", roverMoved), 3000,
							Position.TOP_CENTER);

				} else {

					RoverInitializedBroadCastEventDto dto = SerializeUtils.readFromBroadCast(newMessage);
					ApexCharts bubbleChart = plateauToChart.get(dto.getPlateauId());

					// add rover series to current state
					plateauToSeries.putIfAbsent(dto.getPlateauId(), new TreeMap<>());
					plateauToSeries.get(dto.getPlateauId()).put(dto.getName(), toSeries(dto));

					// update bubbleChart with current state and sorted by roverName
					List<Series<Double[]>> series = plateauToSeries.get(dto.getPlateauId()).values().stream()
							.sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
							.collect(Collectors.toList());
					bubbleChart.updateSeries(series.toArray(new Series[series.size()]));

					// show notification of new bubble
					Notification.show(String.format("A rover has just been added %s", dto), 3000, Position.TOP_CENTER);
				}

			});
		});
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		broadcasterRegistration.remove();
		broadcasterRegistration = null;
	}

}

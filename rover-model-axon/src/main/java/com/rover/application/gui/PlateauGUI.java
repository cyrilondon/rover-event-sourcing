package com.rover.application.gui;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.queryhandling.QueryGateway;

import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauCommandService;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
import com.vaadin.annotations.Push;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;

@SpringUI
@Push
public class PlateauGUI extends UI {

	private static final long serialVersionUID = 1L;

	private final PlateauCommandService plateauCommandService;

	private final PlateauCommandMapper plateauCommandMapper;

	private PlateauSummaryDataProvider plateauSummaryDataProvider;

	private final QueryGateway queryGateway;

	Navigator navigator;
	protected static final String CHARTVIEW = "chart";
	protected static final String GRIDVIEW = "";

	public PlateauGUI(PlateauCommandService plateauCommandService, PlateauCommandMapper plateauCommandMapper,
			PlateauSummaryDataProvider plateauSummaryDataProvider, QueryGateway queryGateway) {
		this.plateauCommandService = plateauCommandService;
		this.plateauCommandMapper = plateauCommandMapper;
		this.plateauSummaryDataProvider = plateauSummaryDataProvider;
		this.queryGateway = queryGateway;
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Event-driven and reactive Rover application");

		// Create a navigator to control the views
		navigator = new Navigator(this, this);

		// Create and register the views
		navigator.addView(GRIDVIEW, new PlateauGridView());
	}
	
	public class PlateauGridView extends VerticalLayout implements View {

		public PlateauGridView() {
			HorizontalLayout commandBar = new HorizontalLayout();
			commandBar.setWidth("100%");
			commandBar.addComponents(createPlateauPanel(), desactivatePlateauPanel());

			VerticalLayout layout = new VerticalLayout();
			layout.addComponents(commandBar, summaryLayout());
			layout.setHeight(95, Unit.PERCENTAGE);

			addComponent(layout);
		}

	}

	// The panel for initializing a Plateau
	private Panel createPlateauPanel() {

		TextField width = new TextField("width");
		TextField height = new TextField("height");
		Button submit = new Button("Create Plateau");

		submit.addClickListener(event -> {
			// send the command
			PlateauInitializeCmd cmd = plateauCommandMapper.toPlateauInitializeCmd(Integer.parseInt(width.getValue()),
					Integer.parseInt(height.getValue()));
			CompletableFuture<UUID> result = plateauCommandService.initializePlateau(cmd);

			// show notification to the user
			result.whenComplete((msg, ex) -> {
				// show notification to the user
				if (ex != null) {
					Notification.show(String.format("%s [entity Plateau could not be created]", ex.getMessage()),
							Notification.Type.ERROR_MESSAGE);

				} else {
					Notification.show(String.format("Plateau id [%s] successfully created", msg.toString()),
							Notification.Type.HUMANIZED_MESSAGE);
				}
			});
		});

		FormLayout form = new FormLayout();
		form.setMargin(true);
		form.addComponents(width, height, submit);

		Panel panel = new Panel("Initialize Plateau");
		panel.setContent(form);
		panel.setHeight(300, Unit.PIXELS);
		return panel;
	}

	// The panel for desactivating a given Plateau
	private Panel desactivatePlateauPanel() {
		TextField plateauId = new TextField("plateau id");
		Button submit = new Button("Desactivate Plateau");

		submit.addClickListener(event -> {
			// send the command
			PlateauDesactivateCmd cmd = plateauCommandMapper.toPlateauDesactivateCmd(plateauId.getValue());
			CompletableFuture<UUID> result = plateauCommandService.desactivatePlateau(cmd);

			result.whenComplete((msg, ex) -> {
				// show notification to the user
				if (ex != null) {
					Notification.show(String.format("%s [Plateau id = %s]", ex.getMessage(), plateauId.getValue()),
							Notification.Type.ERROR_MESSAGE);

				} else {
					Notification.show(String.format("Plateau id [%s] desactivated", plateauId.getValue()),
							Notification.Type.HUMANIZED_MESSAGE);
				}
			});

		});

		FormLayout form = new FormLayout();
		form.setMargin(true);
		form.addComponents(plateauId, submit);

		Panel panel = new Panel("Desactivate Plateau");
		panel.setContent(form);
		panel.setHeight(300, Unit.PIXELS);
		return panel;
	}

	private Layout summaryLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		Grid summaryGrid = summaryGrid();
		layout.addComponent(summaryGrid);
		layout.setExpandRatio(summaryGrid, 1);
		return layout;
	}

	private Grid summaryGrid() {
		plateauSummaryDataProvider = new PlateauSummaryDataProvider(queryGateway);
		Grid<PlateauSummary> grid = new Grid<>();
		Grid.Column<PlateauSummary, String> idColumn = grid.addColumn(PlateauSummary::getId).setCaption("Plateau ID");
		grid.addColumn(PlateauSummary::getWidth).setCaption("Width");
		grid.addColumn(PlateauSummary::getHeight).setCaption("Height");
		grid.addColumn(PlateauSummary::getStatus).setCaption("Status");

		HeaderRow filterRow = grid.appendHeaderRow();
		TextField idStartsWith = new TextField();
		idStartsWith.setValueChangeMode(ValueChangeMode.EAGER);
		idStartsWith.setPlaceholder("Starting with");
		idStartsWith.addValueChangeListener(event -> {
			plateauSummaryDataProvider.setFilter(new PlateauSummaryFilter(event.getValue()));
			plateauSummaryDataProvider.refreshAll();
		});
		filterRow.getCell(idColumn).setComponent(idStartsWith);

		grid.setSizeFull();
		grid.setDataProvider(plateauSummaryDataProvider);

		return grid;
	}

	@Override
	public void close() {
		plateauSummaryDataProvider.close();
		super.close();
	}

}

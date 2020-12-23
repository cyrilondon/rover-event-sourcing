package com.rover.application.gui;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.modelling.command.AggregateNotFoundException;

import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.exception.GameExceptionLabels;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauService;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "plateau", layout = MainLayout.class)
@PageTitle("Plateau Commands")
@CssImport("./styles/views/plateau/plateau-command-view.css")
@RouteAlias(value = "", layout = MainLayout.class)
public class PlateauCommandView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final PlateauService plateauCommandService;

	private final PlateauCommandMapper plateauCommandMapper;

	private PlateauSummaryDataProvider plateauSummaryDataProvider;

	public PlateauCommandView(PlateauService plateauCommandService, PlateauCommandMapper plateauCommandMapper,
			PlateauSummaryDataProvider plateauSummaryDataProvider) {

		this.plateauCommandService = plateauCommandService;
		this.plateauCommandMapper = plateauCommandMapper;
		this.plateauSummaryDataProvider = plateauSummaryDataProvider;

		Div createCmdDiv = createCmdDiv();
		Div desactivateCmdDiv = desactivateCmdDiv();
		HorizontalLayout cmdLayout = new HorizontalLayout(createCmdDiv, desactivateCmdDiv);

		add(cmdLayout, summaryGrid());
	}

	private Div createCmdDiv() {
		TextField width = new TextField("width");
		TextField height = new TextField("height");
		Button createSubmit = new Button("Create Plateau");
		setHorizontalComponentAlignment(Alignment.CENTER, width, height, createSubmit);
		setPadding(true);
		setMargin(true);
		setHeightFull();

		createSubmit.addClickListener(event -> sendInitializePlateauCommand(width.getValue(), height.getValue()));

		return addToCmdDiv(createSubmit, width, height);
	}

	private Div desactivateCmdDiv() {
		TextField plateauId = new TextField("plateauId");
		Button desactivateSubmit = new Button("Desactivate Plateau");
		setHorizontalComponentAlignment(Alignment.CENTER, plateauId, desactivateSubmit);
		setPadding(true);
		setMargin(true);
		setHeightFull();

		desactivateSubmit.addClickListener(event -> sendDesactivatedPlateauCommand(plateauId.getValue()));

		return addToCmdDiv(desactivateSubmit, plateauId);
	}

	private Div addToCmdDiv(Component... components) {
		Div div = new Div();
		VerticalLayout createCmdLayout = new VerticalLayout();
		createCmdLayout.add(components);
		div.add(createCmdLayout);
		div.setClassName("command-panel");
		return div;
	}

	private Grid<PlateauSummary> summaryGrid() {
		Grid<PlateauSummary> grid = new Grid<>();
		Column<PlateauSummary> idColumn = grid.addColumn(PlateauSummary::getId).setHeader("Plateau ID");
		grid.addColumn(PlateauSummary::getWidth).setHeader("Width");
		grid.addColumn(PlateauSummary::getHeight).setHeader("Height");
		grid.addColumn(PlateauSummary::getStatus).setHeader("Status");

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

	private void sendInitializePlateauCommand(String width, String height) {
		// send the command
		PlateauInitializeCmd cmd = plateauCommandMapper.toPlateauInitializeCmd(Integer.parseInt(width),
				Integer.parseInt(height));
		CompletableFuture<UUID> result = plateauCommandService.initializePlateau(cmd);

		handleResult(result, "Plateau id [%s] successfully created", "Aggregate Plateau could not be created: %s",
				true);
	}

	private void sendDesactivatedPlateauCommand(String plateauId) {
		// send the command
		PlateauDesactivateCmd cmd = plateauCommandMapper.toPlateauDesactivateCmd(plateauId);
		CompletableFuture<UUID> result = plateauCommandService.desactivatePlateau(cmd);

		handleResult(result, String.format("Plateau id [%s] successfully desactivated", plateauId),
				"Aggregate Plateau could not be desactivated: %s", false);
	}

	private void handleResult(CompletableFuture<UUID> result, String successMsg, String errorMsg,
			boolean expectedResult) {
		// show notification to the user
		result.whenComplete((msg, ex) -> {
			// show notification to the user
			if (ex != null) {
				String exMsg = ex.getMessage();
				if (ex instanceof AggregateNotFoundException) {
					exMsg = "[" + GameExceptionLabels.ENTITY_NOT_FOUND_ERROR_CODE + "] " + exMsg;
				}
				Div content = new Div();
				content.addClassName("notification-error-msg");
				content.setText(String.format(errorMsg, exMsg));
				Notification notification = new Notification(content);
				notification.setDuration(3000);
				notification.setPosition(Position.TOP_CENTER);
				notification.open();

			} else {
				if (expectedResult) {
					Notification.show(String.format(successMsg, msg.toString()), 3000, Position.TOP_CENTER);
				} else {
					Notification.show(successMsg, 2000, Position.TOP_CENTER);
				}
			}
		});
	}
}

package com.rover.application.gui;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.queryhandling.QueryGateway;

import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauCommandService;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;
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
import com.vaadin.flow.component.page.Push;
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

	private TextField width;
	private TextField height;
	private Button createSubmit;

	private final PlateauCommandService plateauCommandService;

	private final PlateauCommandMapper plateauCommandMapper;
	
	private PlateauSummaryDataProvider plateauSummaryDataProvider;
	
	private final QueryGateway queryGateway;

	public PlateauCommandView(PlateauCommandService plateauCommandService, PlateauCommandMapper plateauCommandMapper, PlateauSummaryDataProvider plateauSummaryDataProvider, QueryGateway queryGateway) {

		this.plateauCommandService = plateauCommandService;
		this.plateauCommandMapper = plateauCommandMapper;
		this.plateauSummaryDataProvider = plateauSummaryDataProvider;
		this.queryGateway = queryGateway;

		width = new TextField("width");
		height = new TextField("height");
		
		Button createSubmit = new Button("Create Plateau");
		setHorizontalComponentAlignment(Alignment.CENTER, width, height);
		setHorizontalComponentAlignment(Alignment.CENTER, createSubmit);
		setPadding(true);
		setMargin(true);
		setHeightFull();

		createSubmit.addClickListener(event -> sendInitializePlateauCommand());
		
		Div div = new Div();
		VerticalLayout createCmdLayout = new VerticalLayout();
		createCmdLayout.add(createSubmit, width, height);
		div.add(createCmdLayout);
		div.setClassName("command-panel");	
		
		add(div, summaryGrid());

	}
	
	 private Grid summaryGrid() {
			plateauSummaryDataProvider = new PlateauSummaryDataProvider(queryGateway);
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


	private void sendInitializePlateauCommand() {
		// send the command
		PlateauInitializeCmd cmd = plateauCommandMapper.toPlateauInitializeCmd(Integer.parseInt(width.getValue()),
				Integer.parseInt(height.getValue()));
		CompletableFuture<UUID> result = plateauCommandService.initializePlateau(cmd);

		handleResult(result);
	}

	private void handleResult(CompletableFuture<UUID> result) {
		// show notification to the user
		result.whenComplete((msg, ex) -> {
			// show notification to the user
			if (ex != null) {
				Div content = new Div();
				content.addClassName("notification-error-msg");
				content.setText(String.format("%s [entity Plateau could not be created]", ex.getMessage()));
				Notification notification = new Notification(content);
				notification.setDuration(3000);
				notification.setPosition(Position.TOP_CENTER);
				notification.open();

			} else {
				Notification.show(String.format("Plateau id [%s] successfully created", msg.toString()), 5000,
						Position.TOP_CENTER);
			}
		});
	}
}
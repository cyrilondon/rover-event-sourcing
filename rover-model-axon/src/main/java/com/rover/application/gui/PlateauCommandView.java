package com.rover.application.gui;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauCommandService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

	public PlateauCommandView(PlateauCommandService plateauCommandService, PlateauCommandMapper plateauCommandMapper) {

		this.plateauCommandService = plateauCommandService;
		this.plateauCommandMapper = plateauCommandMapper;
		
		setClassName("command-panel");	

		width = new TextField("width");
		height = new TextField("height");
		
		Button createSubmit = new Button("Create Plateau");
		add(createSubmit, width, height);
		setHorizontalComponentAlignment(Alignment.CENTER, width, height);
		setHorizontalComponentAlignment(Alignment.CENTER, createSubmit);
		setPadding(true);
		setMargin(true);
		setWidth("20%");

		createSubmit.addClickListener(event -> sendInitializePlateauCommand());

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
				notification.setDuration(5000);
				notification.setPosition(Position.TOP_CENTER);
				notification.open();

			} else {
				Notification.show(String.format("Plateau id [%s] successfully created", msg.toString()), 5000,
						Position.TOP_CENTER);
			}
		});
	}
}

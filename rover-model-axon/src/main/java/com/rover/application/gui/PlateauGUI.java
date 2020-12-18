package com.rover.application.gui;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauCommandService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class PlateauGUI extends UI {

	private static final long serialVersionUID = 1L;

	private final PlateauCommandService plateauCommandService;

	private final PlateauCommandMapper plateauCommandMapper;

	public PlateauGUI(PlateauCommandService plateauCommandService, PlateauCommandMapper plateauCommandMapper) {
		this.plateauCommandService = plateauCommandService;
		this.plateauCommandMapper = plateauCommandMapper;
	}

	@Override
	protected void init(VaadinRequest request) {
		HorizontalLayout plateauBar = new HorizontalLayout();
		plateauBar.setWidth("100%");
		plateauBar.addComponents(createPlateauPanel(), desactivatePlateauPanel());
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponents(plateauBar);

		setContent(layout);
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
					Notification.show(
							String.format("%s [Plateau id = %s]", ex.getMessage(), plateauId.getValue()),
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

}
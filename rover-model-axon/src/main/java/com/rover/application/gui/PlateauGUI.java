package com.rover.application.gui;

import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauCommandService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

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
		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.setWidth("100%");
		commandBar.addComponents(createPlateauPanel());
		
		 setContent(commandBar);
	}

	private Panel createPlateauPanel() {
		TextField width = new TextField("width");
		TextField height = new TextField("height");
		Button submit = new Button("Create Plateau");

		submit.addClickListener(event -> {
			PlateauInitializeCmd cmd = plateauCommandMapper.toPlateauInitializeCmd(Integer.parseInt(width.getValue()),
					Integer.parseInt(height.getValue()));
			plateauCommandService.initializePlateau(cmd);
		});

		FormLayout form = new FormLayout();
		form.setMargin(true);
		form.addComponents(width, height, submit);

		Panel panel = new Panel("Plateau");
		panel.setContent(form);
		return panel;
	}

}

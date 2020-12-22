package com.rover.application.gui;

import org.axonframework.queryhandling.QueryGateway;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "plateau_charts", layout = MainLayout.class)
@PageTitle("Plateau Charts")
public class PlateauChartsView extends HorizontalLayout {
	
private PlateauSummaryDataProvider plateauSummaryDataProvider;
	
	private final QueryGateway queryGateway;

    public PlateauChartsView(PlateauSummaryDataProvider plateauSummaryDataProvider, QueryGateway queryGateway) {
    	this.plateauSummaryDataProvider = plateauSummaryDataProvider;
		this.queryGateway = queryGateway;
		setHeightFull();
        setId("plateau-charts-view");
        add(new Text("Soon you will see beautiful charts here..."));
    }
    
 
}

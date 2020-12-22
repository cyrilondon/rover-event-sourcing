package com.rover.application.gui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "plateau_charts", layout = MainLayout.class)
@PageTitle("Plateau Charts")
public class PlateauChartsView extends Div {

    public PlateauChartsView() {
        setId("plateau-charts-view");
        add(new Text("Soon you will see beautiful charts here..."));
    }

}

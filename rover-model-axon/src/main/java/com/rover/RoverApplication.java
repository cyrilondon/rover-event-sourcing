package com.rover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vaadin.artur.helpers.LaunchUtil;


@SpringBootApplication
public class RoverApplication {

	public static void main(String[] args) {
		  LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(RoverApplication.class, args));
	}

}

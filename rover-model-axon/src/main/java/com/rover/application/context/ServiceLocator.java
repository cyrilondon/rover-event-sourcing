package com.rover.application.context;

import java.util.HashMap;
import java.util.Map;

import com.rover.domain.command.model.entity.plateau.PlateauValidator;
import com.rover.domain.command.model.entity.rover.RoverValidator;
import com.rover.domain.command.model.validation.EntityValidator;

/**
 * Shows an alternative way of Spring injection, here just for illustration purpose.
 * Read this good article from Martin Fowler guru
 * https://martinfowler.com/articles/injection.html
 *
 */
public class ServiceLocator {

	public static String PLATEAU_VALIDATOR = "plateau_validator";
	
	public static String ROVER_VALIDATOR = "rover_validator";

	private Map<String, EntityValidator> entityValidators = new HashMap<>();

	private static ServiceLocator soleInstance = new ServiceLocator();
	
	public static PlateauValidator getPlateauValidator() {
		return (PlateauValidator) soleInstance.entityValidators.get(PLATEAU_VALIDATOR);
	}
	
	public static RoverValidator getRoverValidator() {
		return (RoverValidator) soleInstance.entityValidators.get(ROVER_VALIDATOR);
	}

	public static void load(ServiceLocator arg) {
		soleInstance = arg;
	}

	public void loadEntityValidator(String key, EntityValidator validator) {
		entityValidators.put(key, validator);
	}
	
}

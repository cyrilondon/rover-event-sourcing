package com.rover.application.context;

import java.util.HashMap;
import java.util.Map;

import com.rover.domain.model.entity.plateau.PlateauValidator;
import com.rover.domain.model.validation.EntityValidator;

public class ServiceLocator {

	public static String PLATEAU_VALIDATOR = "plateau_validator";

	private Map<String, EntityValidator> entityValidators = new HashMap<>();

	private static ServiceLocator soleInstance = new ServiceLocator();
	
	public static PlateauValidator getPlateauValidator() {
		return (PlateauValidator) soleInstance.entityValidators.get(PLATEAU_VALIDATOR);
	}

	public static void load(ServiceLocator arg) {
		soleInstance = arg;
	}

	public void loadEntityValidator(String key, EntityValidator validator) {
		entityValidators.put(key, validator);
	}
	
}

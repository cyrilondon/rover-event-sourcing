package com.rover.domain.model.entity.plateau;

import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.model.exception.GameExceptionLabels;
import com.rover.domain.model.validation.EntityDefaultValidationNotificationHandler;
import com.rover.domain.model.validation.EntityValidator;
import com.rover.domain.model.validation.ValidationNotificationHandler;

public class PlateauValidator implements EntityValidator {

	private ValidationNotificationHandler validationHandler = new EntityDefaultValidationNotificationHandler();

	public void doValidate(PlateauInitializeCmd cmd) {
		if (cmd.getWidth() <= 0)
			this.validationHandler
					.handleError(String.format(GameExceptionLabels.PLATEAU_NEGATIVE_WIDTH, cmd.getWidth()));

		if (cmd.getHeight() <= 0)
			this.validationHandler
					.handleError(String.format(GameExceptionLabels.PLATEAU_NEGATIVE_HEIGHT, cmd.getHeight()));
		
		validationHandler.checkValidationResult();
	}

}

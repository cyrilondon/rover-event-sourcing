package com.rover.domain.command.model.entity.plateau;

import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.exception.GameExceptionLabels;
import com.rover.domain.command.model.validation.EntityDefaultValidationNotificationHandler;
import com.rover.domain.command.model.validation.EntityValidator;
import com.rover.domain.command.model.validation.ValidationNotificationHandler;

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

	public void doValidateDesactivated(PlateauDesactivateCmd cmd, PlateauStatus status) {
		if (status == PlateauStatus.INACTIVE)
			this.validationHandler.handleError(String.format(GameExceptionLabels.PLATEAU_ALREADY_DESACTIVATED, cmd.getId().toString()));

		validationHandler.checkValidationResult();
	}

}

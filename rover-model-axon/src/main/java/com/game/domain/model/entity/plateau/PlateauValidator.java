package com.game.domain.model.entity.plateau;

import com.game.domain.model.exception.GameExceptionLabels;
import com.game.domain.model.validation.EntityDefaultValidationNotificationHandler;
import com.game.domain.model.validation.EntityValidator;
import com.game.domain.model.validation.ValidationNotificationHandler;
import com.game.domain.rover.api.PlateauInitializeCmd;

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

package com.rover.domain.command.model.validation;

import com.rover.domain.command.model.exception.EntityValidationException;

public class EntityDefaultValidationNotificationHandler implements ValidationNotificationHandler {

	protected ValidationResult validationResult = new ValidationResult();

	@Override
	public void handleError(String errorMessage) {
		validationResult.addErrorMessage(errorMessage);
	}

	@Override
	public void checkValidationResult() {
		if (validationResult.isInError()) {
			String allErrorMessages = validationResult.getAllErrorMessages();
			validationResult.reset();
			throw new EntityValidationException(allErrorMessages);
		}

	}

}

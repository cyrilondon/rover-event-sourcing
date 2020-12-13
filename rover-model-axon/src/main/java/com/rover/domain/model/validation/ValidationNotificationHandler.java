package com.rover.domain.model.validation;

public interface ValidationNotificationHandler {
	
	public void handleError(String errorMessage) ;
	
	public void checkValidationResult();

}

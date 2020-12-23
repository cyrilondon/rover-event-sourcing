package com.rover.domain.command.model.validation;

public interface EntityValidator {
	
	 public default ValidationNotificationHandler validationNotificationHandler() {
	        return new EntityDefaultValidationNotificationHandler();
	    }

}

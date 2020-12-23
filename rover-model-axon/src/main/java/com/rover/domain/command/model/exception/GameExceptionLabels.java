package com.rover.domain.command.model.exception;

/**
 * We externalize all error messages and code in this class to have a better
 * visibility of all possible error scenarios in the game.
 *
 */
public class GameExceptionLabels {

	/**
	 * Final error message example: [ERR-000] Broken precondition: Missing Rover
	 * name
	 */
	public static final String ERROR_CODE_AND_MESSAGE_PATTERN = "[%s] %s";

	public static final String ERROR_MESSAGE_SEPARATION_PATTERN = "%s - %s";
	
	public static final String ENTITY_NOT_FOUND_ERROR_CODE = "ERR-000";
	
	public static final String ENTITY_VALIDATION_ERROR_CODE = "ERR-001";

	public static final String ILLEGAL_ARGUMENT_CODE = "ERR-002";

	public static final String PRE_CHECK_ERROR_MESSAGE = "Broken precondition: %s";

	public static final String MISSING_PLATEAU_DIMENSIONS = "Missing Plateau dimensions";

	public static final String MISSING_PLATEAU_CONFIGURATION = "Missing Plateau configuration";
	
	public static final String MISSING_PLATEAU_UUID = "Missing Plateau identifiant";

	public static final String PLATEAU_NEGATIVE_WIDTH = "Plateau width [%d] should be strictly positive";

	public static final String PLATEAU_NEGATIVE_HEIGHT = "Plateau height [%d] should be strictly positive";
	
	
	

	



}

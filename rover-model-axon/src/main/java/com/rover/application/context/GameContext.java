package com.rover.application.context;

import com.rover.domain.command.model.entity.plateau.PlateauValidator;
import com.rover.domain.command.model.entity.rover.RoverValidator;

/**
 * Application context whose responsibility is to keep track of the game state.
 * Exposed as a singleton to the rest of the application via
 * {@link GameContext#getInstance()} or
 * {@link GameContext#getInstance(int step)} if we want the Rover to move with a
 * step length different than the default one = 1 Equivalent to an Application
 * Spring context Provides the other different application and domain services
 * via Dependency Injection/ Inversion of Control
 */
public class GameContext {

	/**
	 * By default, the rover moves by one step forward
	 */
	public static final int ROVER_STEP_LENGTH = 1;

	private int roverStepLength = ROVER_STEP_LENGTH;

	private static GameContext GAME_CONTEXT = new GameContext();

	private GameContext() {
		configure();
	}

	/**
	 * Configure the game with the on-demand implementations
	 */
	private void configure() {
		ServiceLocator locator = new ServiceLocator();
		locator.loadEntityValidator(ServiceLocator.PLATEAU_VALIDATOR, new PlateauValidator());
		locator.loadEntityValidator(ServiceLocator.ROVER_VALIDATOR, new RoverValidator());
		ServiceLocator.load(locator);
	}

	public static GameContext getInstance() {
		return GAME_CONTEXT;
	}

	public static GameContext getInstance(int step) {
		GAME_CONTEXT.roverStepLength = step;
		return GAME_CONTEXT;
	}

	public PlateauValidator getPlateauValidator() {
		return ServiceLocator.getPlateauValidator();
	}
	
	public RoverValidator getRoverValidator() {
		return ServiceLocator.getRoverValidator();
	}

}

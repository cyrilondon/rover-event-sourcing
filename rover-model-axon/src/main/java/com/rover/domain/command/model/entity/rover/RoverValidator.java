package com.rover.domain.command.model.entity.rover;

import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.command.model.exception.GameExceptionLabels;
import com.rover.domain.command.model.validation.EntityValidator;
import com.rover.domain.command.model.validation.ValidationNotificationHandler;

public class RoverValidator implements EntityValidator {


	protected void doValidate(RoverInitializeCmd cmd) {
		
		ValidationNotificationHandler notificationHandler = validationNotificationHandler();

		if (isXPositionNegative(cmd))
			notificationHandler
					.handleError(String.format(GameExceptionLabels.ROVER_NEGATIVE_X, cmd.getPosition().getAbscissa()));

		if (isYPositionNegative(cmd))
			notificationHandler
					.handleError(String.format(GameExceptionLabels.ROVER_NEGATIVE_Y, cmd.getPosition().getOrdinate()));
		
		notificationHandler.checkValidationResult();

//		if (isXPositionOutOfBoard())
//			this.notificationHandler().handleError(String.format(GameExceptionLabels.ROVER_X_OUT_OF_PLATEAU,
//					entity().getXPosition(), GameContext.getInstance().getPlateau(entity().getId().getPlateauId()).getWidth()));
//
//		if (isYPositionOutOfBoard())
//			this.notificationHandler().handleError(String.format(GameExceptionLabels.ROVER_Y_OUT_OF_PLATEAU,
//					entity().getYPosition(), GameContext.getInstance().getPlateau(entity().getId().getPlateauId()).getHeight()));
//
//		if (areBothCoordinatesPositive() && areBothCoordinatesInsideTheBoard() && positionAlreadyBusy())
//			this.notificationHandler().handleError(String.format(GameExceptionLabels.PLATEAU_LOCATION_ALREADY_SET,
//					entity().getXPosition(), entity().getYPosition()));

	}


	private boolean isXPositionNegative(RoverInitializeCmd cmd) {
		return cmd.getPosition().getAbscissa() < 0;
	}

	private boolean isYPositionNegative(RoverInitializeCmd cmd) {
		return cmd.getPosition().getOrdinate() < 0;
	}

	private boolean areBothCoordinatesPositive(RoverInitializeCmd cmd) {
		return !(isXPositionNegative(cmd) || isYPositionNegative(cmd));
	}

//	private boolean isXPositionOutOfBoard() {
//		return entity().getXPosition() > GameContext.getInstance().getPlateau(entity().getId().getPlateauId()).getWidth();
//	}
//
//	private boolean isYPositionOutOfBoard() {
//		return entity().getYPosition() > GameContext.getInstance().getPlateau(entity().getId().getPlateauId()).getHeight();
//	}
//
//	private boolean areBothCoordinatesInsideTheBoard() {
//		return !(isXPositionOutOfBoard() || isYPositionOutOfBoard());
//	}
//	
//	private boolean positionAlreadyBusy() {
//		return GameContext.getInstance().getPlateau(entity().getId().getPlateauId())
//				.isLocationBusy(new TwoDimensionalCoordinates(entity().getXPosition(), entity().getYPosition()));
//	}

}

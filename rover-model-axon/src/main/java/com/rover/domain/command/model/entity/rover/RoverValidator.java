package com.rover.domain.command.model.entity.rover;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.springframework.stereotype.Component;

import com.rover.core.util.SerializeUtils;
import com.rover.domain.api.DomainEventRepository;
import com.rover.domain.command.model.entity.plateau.PlateauDto;
import com.rover.domain.command.model.exception.GameExceptionLabels;
import com.rover.domain.command.model.validation.EntityValidator;
import com.rover.domain.command.model.validation.ValidationNotificationHandler;

@Component
public class RoverValidator implements EntityValidator {

	private final DomainEventRepository eventRepository;

	/**
	 * We need to inject the event repository here to fetch the corresponding Plateau
	 * as we want to validate the rover position against the plateau dimensions
	 * @param eventRepository
	 */
	public RoverValidator(DomainEventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	
	protected void doValidate(String plateauId, int abscissa, int ordinate) {

		ValidationNotificationHandler notificationHandler = validationNotificationHandler();

		List<DomainEventEntry> plateaus = eventRepository.findByAggregateIdentifier(plateauId);

		if (isXPositionNegative(abscissa))
			notificationHandler
					.handleError(String.format(GameExceptionLabels.ROVER_NEGATIVE_X, abscissa));

		if (isYPositionNegative(ordinate))
			notificationHandler
					.handleError(String.format(GameExceptionLabels.ROVER_NEGATIVE_Y, ordinate));

		if (plateaus.isEmpty()) {
			notificationHandler.handleError(GameExceptionLabels.INITIALIZE_ROVER_NOT_ALLOWED);
		} else {
			
			DomainEventEntry plateau = plateaus.get(0);
			String payload = new String(plateau.getPayload().getData(), StandardCharsets.UTF_8);
			PlateauDto dto = SerializeUtils.readFromEvent(payload);
			if (isXPositionOutOfBoard(abscissa, dto)) {
				notificationHandler.handleError(String.format(GameExceptionLabels.ROVER_X_OUT_OF_PLATEAU,
						abscissa, dto.getWidth()));
			}
			
			if (isYPositionOutOfBoard(ordinate, dto))
				notificationHandler.handleError(String.format(GameExceptionLabels.ROVER_Y_OUT_OF_PLATEAU,
						ordinate, dto.getHeight()));
			
		}

		notificationHandler.checkValidationResult();

	}

	private boolean isXPositionNegative(int abscissa) {
		return abscissa < 0;
	}

	private boolean isYPositionNegative(int ordinate) {
		return ordinate < 0;
	}
	
	private boolean isXPositionOutOfBoard(int abscissa, PlateauDto plateau) {
		return abscissa > plateau.getWidth();
	}
	
	private boolean isYPositionOutOfBoard(int  ordinate, PlateauDto plateau) {
		return ordinate > plateau.getHeight();
	}
	

}

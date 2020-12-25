package com.rover.domain.command.model.entity.rover;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.springframework.stereotype.Component;

import com.rover.domain.api.DomainEventRepository;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.command.model.entity.plateau.PlateauDto;
import com.rover.domain.command.model.entity.plateau.PlateauUtils;
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

	protected void doValidate(RoverInitializeCmd cmd) {

		ValidationNotificationHandler notificationHandler = validationNotificationHandler();

		List<DomainEventEntry> plateaus = eventRepository.findByAggregateIdentifier(cmd.getId().getPlateauId().toString());

		if (isXPositionNegative(cmd))
			notificationHandler
					.handleError(String.format(GameExceptionLabels.ROVER_NEGATIVE_X, cmd.getPosition().getAbscissa()));

		if (isYPositionNegative(cmd))
			notificationHandler
					.handleError(String.format(GameExceptionLabels.ROVER_NEGATIVE_Y, cmd.getPosition().getOrdinate()));

		if (plateaus.isEmpty()) {
			notificationHandler.handleError(GameExceptionLabels.INITIALIZE_ROVER_NOT_ALLOWED);
		} else {
			
			DomainEventEntry plateau = plateaus.get(0);
			String payload = new String(plateau.getPayload().getData(), StandardCharsets.UTF_8);
			PlateauDto dto = PlateauUtils.readFromEvent(payload);
			if (isXPositionOutOfBoard(cmd, dto)) {
				notificationHandler.handleError(String.format(GameExceptionLabels.ROVER_X_OUT_OF_PLATEAU,
						cmd.getPosition().getAbscissa(), dto.getWidth()));
			}
			
			if (isYPositionOutOfBoard(cmd, dto))
				notificationHandler.handleError(String.format(GameExceptionLabels.ROVER_Y_OUT_OF_PLATEAU,
						cmd.getPosition().getOrdinate(), dto.getHeight()));
			
		}

		notificationHandler.checkValidationResult();

	}

	private boolean isXPositionNegative(RoverInitializeCmd cmd) {
		return cmd.getPosition().getAbscissa() < 0;
	}

	private boolean isYPositionNegative(RoverInitializeCmd cmd) {
		return cmd.getPosition().getOrdinate() < 0;
	}
	
	private boolean isXPositionOutOfBoard(RoverInitializeCmd cmd, PlateauDto plateau) {
		return cmd.getPosition().getAbscissa() > plateau.getWidth();
	}
	
	private boolean isYPositionOutOfBoard(RoverInitializeCmd cmd, PlateauDto plateau) {
		return cmd.getPosition().getOrdinate() > plateau.getHeight();
	}
	

}

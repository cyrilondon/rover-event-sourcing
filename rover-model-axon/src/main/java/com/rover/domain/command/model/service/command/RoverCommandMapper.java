package com.rover.domain.command.model.service.command;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.rover.application.command.dto.RoverInitializeCmdDto;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates;
import com.rover.domain.command.model.entity.rover.Orientation;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;

@Component
public class RoverCommandMapper {
	
	public RoverInitializeCmd toRoverInitializeCmd(RoverInitializeCmdDto dto){
		RoverIdentifier roverId = new RoverIdentifier(UUID.fromString(dto.getPlateauUuid()), dto.getRoverName());
		Orientation orientation = Orientation.get(dto.getOrientation());
		TwoDimensionalCoordinates position = new TwoDimensionalCoordinates(dto.getAbscissa(), dto.getOrdinate());
		return new RoverInitializeCmd(roverId, position, orientation);
	}

}

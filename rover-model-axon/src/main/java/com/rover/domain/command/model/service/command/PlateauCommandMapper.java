package com.rover.domain.command.model.service.command;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.rover.application.command.dto.plateau.PlateauInitializeCmdDto;
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;

@Component
public class PlateauCommandMapper {
	
	public PlateauInitializeCmd toPlateauInitializeCmd(PlateauInitializeCmdDto plateauInitializeCmdDto){
		return new PlateauInitializeCmd(UUID.randomUUID(), plateauInitializeCmdDto.getWidth(), plateauInitializeCmdDto.getHeight());
	}
	
	public PlateauInitializeCmd toPlateauInitializeCmd(int width, int height){
		return new PlateauInitializeCmd(UUID.randomUUID(), width, height);
	}
	
	public PlateauDesactivateCmd toPlateauDesactivateCmd(String plateauId) {
		return new PlateauDesactivateCmd(UUID.fromString(plateauId));
	}

}

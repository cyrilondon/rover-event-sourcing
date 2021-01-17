package com.rover.domain.command.model.entity.rover;

public class RoverRemovedEventDto {

	String name;

	String plateauId;
	
	public RoverRemovedEventDto(String name, String plateauId) {
		this.name = name;
		this.plateauId = plateauId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlateauId() {
		return plateauId;
	}

	public void setPlateauId(String plateauId) {
		this.plateauId = plateauId;
	}

}

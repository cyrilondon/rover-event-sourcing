package com.rover.domain.command.model.entity.rover;

public class RoverInitializedBroadCastEventDto {
	
	String name;
	
	String plateauId;
	
	int abscissa, ordinate;
	
	public RoverInitializedBroadCastEventDto(String name, String plateauId, int abscissa, int ordinate) {
		this.name = name;
		this.plateauId = plateauId;
		this.abscissa = abscissa;
		this.ordinate = ordinate;
	}

	public String getPlateauId() {
		return plateauId;
	}

	public String getName() {
		return name;
	}

	public int getAbscissa() {
		return abscissa;
	}

	public int getOrdinate() {
		return ordinate;
	}
	
	@Override
	public String toString() {
		return String.format("name [%s] plateau [%s] abscissa [%s] ordinate [%s]", name, plateauId, abscissa, ordinate);
	}
	
	

}

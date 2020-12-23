package com.rover.application.command.dto;

public class RoverInitializeCmdDto {
	
	private int abscissa, ordinate;
	
	private String orientation;
	
	private String plateauUuid;
	
	private String roverName;

	public String getRoverName() {
		return roverName;
	}

	public void setRoverName(String roverName) {
		this.roverName = roverName;
	}

	public int getAbscissa() {
		return abscissa;
	}

	public void setAbscissa(int abscissa) {
		this.abscissa = abscissa;
	}

	public int getOrdinate() {
		return ordinate;
	}

	public void setOrdinate(int ordinate) {
		this.ordinate = ordinate;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getPlateauUuid() {
		return plateauUuid;
	}

	public void setPlateauUuid(String plateauUuid) {
		this.plateauUuid = plateauUuid;
	}

}

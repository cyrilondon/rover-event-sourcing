package com.rover.application.command.dto;

public class RoverInitializeCmdDto {
	
	private int abscissa, ordinate;
	
	private String orientation;
	
	private String plateauUuid;
	
	private String roverName;
	
	private RoverInitializeCmdDto(Builder builder) {
		this.roverName = builder.roverName;
		this.plateauUuid = builder.plateauUuid;
		this.abscissa = builder.abscissa;
		this.ordinate = builder.ordinate;
		this.orientation = builder.orientation;
	}

	public String getRoverName() {
		return roverName;
	}

	public int getAbscissa() {
		return abscissa;
	}

	public int getOrdinate() {
		return ordinate;
	}

	public String getOrientation() {
		return orientation;
	}

	public String getPlateauUuid() {
		return plateauUuid;
	}
	
	public static class Builder {

		String roverName;

		private int abscissa, ordinate;

		String orientation;

		String plateauUuid;

		public Builder withName(String name) {
			this.roverName = name;
			return this;
		}

		public Builder withPlateauUuid(String uuid) {
			this.plateauUuid = uuid;
			return this;
		}

		public Builder withAbscissa(int x) {
			this.abscissa = x;
			return this;
		}

		public Builder withOrdinate(int y) {
			this.ordinate = y;
			return this;
		}

		public Builder withOrientation(String orientation) {
			this.orientation = orientation;
			return this;
		}

		public RoverInitializeCmdDto build() {
			return new RoverInitializeCmdDto(this);
		}

	}

}

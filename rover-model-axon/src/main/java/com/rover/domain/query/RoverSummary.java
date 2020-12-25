package com.rover.domain.query;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RoverSummary {
	
	@Id
	String roverId;
	
	String orientation;
	
	Integer abscissa, ordinate;
	
	public RoverSummary(){	
	}
	
	public RoverSummary(String roverId, String orientation, Integer abscissa, Integer ordinate) {
		this.roverId = roverId;
		this.orientation = orientation;
		this.abscissa = abscissa;
		this.ordinate = ordinate;
	}

	public String getRoverId() {
		return roverId;
	}

	public void setRoverId(String roverId) {
		this.roverId = roverId;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Integer getAbscissa() {
		return abscissa;
	}

	public void setAbscissa(Integer abscissa) {
		this.abscissa = abscissa;
	}

	public Integer getOrdinate() {
		return ordinate;
	}

	public void setOrdinate(Integer ordinate) {
		this.ordinate = ordinate;
	}


}

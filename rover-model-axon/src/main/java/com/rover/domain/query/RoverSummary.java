package com.rover.domain.query;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(
	    name = "RoverSummary.findByNameAndPlateau",
	    query = "SELECT r FROM RoverSummary r where r.roverName =:roverName and r.plateau.id =:plateauId")
public class RoverSummary {
	
	@Id
	String roverName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	PlateauSummary plateau;
	
	String orientation;
	
	Integer abscissa, ordinate;
	
	public RoverSummary(){	
	}
	
	public RoverSummary(String roverId, PlateauSummary plateau, String orientation, Integer abscissa, Integer ordinate) {
		this.roverName = roverId;
		this.plateau = plateau;
		this.orientation = orientation;
		this.abscissa = abscissa;
		this.ordinate = ordinate;
	}


	public PlateauSummary getPlateau() {
		return plateau;
	}

	public void setPlateau(PlateauSummary plateau) {
		this.plateau = plateau;
	}

	public String getRoverName() {
		return roverName;
	}

	public void setRoverId(String roverId) {
		this.roverName = roverId;
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

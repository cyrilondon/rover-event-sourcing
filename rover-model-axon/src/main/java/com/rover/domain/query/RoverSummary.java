package com.rover.domain.query;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "RoverSummary.findByNameAndPlateau", query = "SELECT r FROM RoverSummary r where r.id.name =:roverName and r.id.plateauId =:plateauId")
public class RoverSummary {

	@EmbeddedId
	private RoverId id;

	String orientation;

	Integer abscissa, ordinate;

	public RoverSummary() {
	}

	public RoverSummary(RoverId id, String orientation, Integer abscissa,
			Integer ordinate) {
		this.id = id;
		this.orientation = orientation;
		this.abscissa = abscissa;
		this.ordinate = ordinate;
	}

	public String getPlateauId() {
		return getId().getPlateauId();
	}


	public String getName() {
		return getId().getName();
	}

	public RoverId getId() {
		return id;
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

	public String toString() {
		return String.format("name: %s, plateau: %s, orientation: %s, abscissa: %s, ordinate: %s ", getName(), getPlateauId(),
				orientation, abscissa, ordinate);
	}

}

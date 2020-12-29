package com.rover.domain.query;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;


/**
 * Rover identifier which includes Plateau UUID + Rover name
 * This combination identifies the Rover with absolute uniqueness
 * Remark: two Rovers can have the same name assuming they each belong to a distinct Plateau
 */
@Embeddable
public class RoverId implements Serializable {
	
	private static final long serialVersionUID = -136210258558861060L;
	
	/**
	 * Many-to-one association to a Plateau instance
	 * We keep track of the plateau UUID
	 */
	private String plateauId;
	
	private String name;
	
	public RoverId() {
		// required for hibernate
	}
	
	public RoverId(String plateauId, String name) {
		this.plateauId = plateauId;
		this.name = name;
	}

	public String getPlateauId() {
		return plateauId;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj instanceof RoverId) {
			RoverId other = (RoverId) obj;
			return Objects.equals(plateauId, other.getPlateauId()) && Objects.equals(name, other.getName());
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getPlateauId(), getName());
	}
	
	@Override
	public String toString() {
		return String.format("Name [%s] - Plateau ID [%s]", name, getPlateauId());
	}

}

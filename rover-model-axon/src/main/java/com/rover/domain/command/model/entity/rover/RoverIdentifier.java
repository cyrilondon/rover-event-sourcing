package com.rover.domain.command.model.entity.rover;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.rover.core.validation.ArgumentCheck;
import com.rover.domain.command.model.exception.GameExceptionLabels;

/**
 * Rover identifier which includes Plateau UUID + Rover name
 * This combination identifies the Rover with absolute uniqueness
 * Remark: two Rovers can have the same name assuming they each belong to a distinct Plateau
 */
public class RoverIdentifier implements Serializable {
	
	private static final long serialVersionUID = -136210258558861060L;
	
	/**
	 * Many-to-one association to a Plateau instance
	 * We keep track of the plateau UUID
	 */
	private UUID plateauId;
	
	private String name;
	
	public RoverIdentifier(UUID plateauId, String name) {
		this.plateauId = ArgumentCheck.preNotNull(plateauId, GameExceptionLabels.MISSING_PLATEAU_UUID);
		this.name = ArgumentCheck.preNotEmpty(name, GameExceptionLabels.MISSING_ROVER_NAME);
	}

	public UUID getPlateauId() {
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

		if (obj instanceof RoverIdentifier) {
			RoverIdentifier other = (RoverIdentifier) obj;
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
		return String.format("Name [%s] - Plateau UUID [%s]", name, getPlateauId());
	}

}

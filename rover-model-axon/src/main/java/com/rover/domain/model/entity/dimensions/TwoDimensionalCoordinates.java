package com.rover.domain.model.entity.dimensions;

import java.util.Objects;

public class TwoDimensionalCoordinates {

	private int abscissa, ordinate;

	public TwoDimensionalCoordinates(int x, int y) {
		this.abscissa = x;
		this.ordinate = y;
	}

	/**
	 * We don't mutate the state, i.e. functional behaviour
	 * @param step
	 * @return new coordinates
	 */
	private TwoDimensionalCoordinates shiftAlongAbscissa(int stepLength) {
		return new TwoDimensionalCoordinates(abscissa + stepLength, ordinate);
	}

	/**
	 * We don't mutate the state, i.e. functional behaviour
	 * @param step
	 * @return new coordinates
	 */
	private TwoDimensionalCoordinates shiftAlongOrdinate(int stepLength) {
		return new TwoDimensionalCoordinates(abscissa, ordinate + stepLength);
	}
	
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj instanceof TwoDimensionalCoordinates) {
			TwoDimensionalCoordinates other = (TwoDimensionalCoordinates) obj;
			return Objects.equals(abscissa, other.getAbscissa()) && Objects.equals(ordinate, other.getOrdinate());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAbscissa(), getOrdinate());
	}

	public int[] getCoordinates() {
		return new int[] { getAbscissa(), getOrdinate() };
	}

	public int getAbscissa() {
		return abscissa;
	}

	public int getOrdinate() {
		return ordinate;
	}
	
	@Override
	public String toString() {
		return String.format("Coordinates [abscissa = %s, ordinate = %s]", getAbscissa(), getOrdinate());
	}

}

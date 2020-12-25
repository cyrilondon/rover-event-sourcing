package com.rover.domain.command.model.entity.plateau;

/**
 * Used to read the properties from serialized/binary format in event store
 *
 */
public class PlateauDto {
	
	String id;
	int width;
	int height;
	
	
	public PlateauDto(String id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}


	public String getId() {
		return id;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

}

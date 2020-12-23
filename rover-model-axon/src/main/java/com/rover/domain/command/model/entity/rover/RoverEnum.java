package com.rover.domain.command.model.entity.rover;

import java.io.Serializable;

public interface RoverEnum<T extends Serializable> {

	T getValue();

}

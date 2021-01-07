package com.rover.domain.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID
import com.rover.domain.command.model.entity.rover.RoverIdentifier
import com.rover.domain.command.model.entity.rover.Orientation
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates

data class PlateauInitializedEvt(val id: UUID, val width: Int, val height: Int)

data class PlateauDesactivatedEvt(val id: UUID)

data class PlateauMarkedLocationsEvt(
	val id: String, val position: TwoDimensionalCoordinates,
	val oldPosition: TwoDimensionalCoordinates
)

data class RoverInitializedEvt(
	val id: RoverIdentifier,
	val position: TwoDimensionalCoordinates,
	val orientation: Orientation
) {
	fun getRoverName(): String {
		return id.getName()
	}
}

data class RoverMovedEvt(
	val id: RoverIdentifier,
	val orientation: Orientation,
	val position: TwoDimensionalCoordinates,
	val oldPosition: TwoDimensionalCoordinates
) {
	fun getRoverName(): String {
		return id.getName()
	}
}

data class RoverRemovedEvt(
	val id: RoverIdentifier
) 






               
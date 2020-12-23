package com.rover.domain.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID
import com.rover.domain.command.model.entity.rover.RoverIdentifier
import com.rover.domain.command.model.entity.rover.Orientation
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates

data class PlateauInitializedEvt(val id: UUID, val width: Int, val height: Int)

data class PlateauDesactivatedEvt(val id: UUID)

data class RoverInitializeEvt(
	val id: RoverIdentifier,
	val position: TwoDimensionalCoordinates,
	val orientation: Orientation
) 





               
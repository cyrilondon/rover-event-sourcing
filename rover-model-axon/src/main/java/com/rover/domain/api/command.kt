package com.rover.domain.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID
import com.rover.domain.command.model.entity.rover.RoverIdentifier
import com.rover.domain.command.model.entity.rover.Orientation
import com.rover.domain.command.model.entity.dimensions.TwoDimensionalCoordinates

interface DomainCommand

data class PlateauInitializeCmd(@TargetAggregateIdentifier val id: UUID, val width: Int, val height: Int) :
	DomainCommand

data class PlateauDesactivateCmd(@TargetAggregateIdentifier val id: UUID) : DomainCommand

data class PlateauMarkLocationsCmd(
	@TargetAggregateIdentifier val id: UUID, val position: TwoDimensionalCoordinates,
	val oldPosition: TwoDimensionalCoordinates
) : DomainCommand

data class RoverInitializeCmd(
	@TargetAggregateIdentifier val id: RoverIdentifier,
	val position: TwoDimensionalCoordinates,
	val orientation: Orientation
) : DomainCommand

data class RoverMoveCmd(
	@TargetAggregateIdentifier val id: RoverIdentifier,
	val orientation: Orientation,
	val steps: Int
) : DomainCommand

data class RoverRemoveCmd(
	@TargetAggregateIdentifier val id: RoverIdentifier
) : DomainCommand







               
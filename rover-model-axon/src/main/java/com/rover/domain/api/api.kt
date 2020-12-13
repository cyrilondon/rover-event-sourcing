package com.rover.domain.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class PlateauInitializeCmd(@TargetAggregateIdentifier val id: UUID, val width: Int, val height: Int)
data class PlateauInitializedEvt(val id: UUID, val width: Int, val height: Int)




               
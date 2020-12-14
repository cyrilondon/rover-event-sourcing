package com.rover.domain.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

interface DomainCommand

data class PlateauInitializeCmd(@TargetAggregateIdentifier val id: UUID, val width: Int, val height: Int) : DomainCommand

data class PlateauDesactivateCmd(@TargetAggregateIdentifier val id: UUID) : DomainCommand







               
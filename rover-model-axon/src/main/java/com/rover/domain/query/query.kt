package com.rover.domain.query

import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

import com.rover.domain.command.model.entity.plateau.PlateauStatus;

@Entity
data class PlateauSummary(@Id var id: String, var status: PlateauStatus) {
    constructor() : this("", PlateauStatus.ACTIVE)
}

interface PlateauSummaryRepository : JpaRepository<PlateauSummary, String>

data class FindPlateauSummaryQuery(val plateauId: String)

class FindAllPlateauSummaryQuery(){ override fun toString() : String = "FindAllPlateauSummaryQuery" }







               
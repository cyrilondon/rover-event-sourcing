package com.rover.domain.query

import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.rover.domain.command.model.entity.plateau.PlateauStatus;

@Entity
@NamedQueries(
        NamedQuery(name = "PlateauSummary.fetch",
                query = "SELECT c FROM PlateauSummary c WHERE c.id LIKE CONCAT(:idStartsWith, '%') ORDER BY c.id"),
        NamedQuery(name = "PlateauSummary.count",
                query = "SELECT COUNT(c) FROM PlateauSummary c WHERE c.id LIKE CONCAT(:idStartsWith, '%')"))
data class PlateauSummary(@Id var id: String, var width: Int, var height: Int, var status: PlateauStatus) {
    constructor() : this("", 0, 0, PlateauStatus.ACTIVE)
}

interface PlateauSummaryRepository : JpaRepository<PlateauSummary, String>

data class FindPlateauSummaryQuery(val plateauId: String)

data class PlateauSummaryFilter(val idStartsWith: String = "")

class CountPlateauSummaryQuery(val filter: PlateauSummaryFilter = PlateauSummaryFilter()) { override fun toString() : String = "CountPlateauSummariesQuery" }
class FindAllPlateauSummaryQuery(val offset: Int, val limit: Int, val filter: PlateauSummaryFilter){ override fun toString() : String = "FindAllPlateauSummaryQuery" }

class PlateauCountChangedUpdate







               
package com.rover.domain.query

import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Entity
data class PlateauSummary(@Id val id: String) {
    constructor() : this("")
}







               
package com.josevg.projetofinalapp.model.Entity

import androidx.room.Embedded
import androidx.room.Relation

data class CarroMotorRelacao (
    @Embedded
    val carro: Carro,
    @Relation(
        parentColumn = "motor",
        entityColumn = "modelo"
    )
    val motor: Motor
)

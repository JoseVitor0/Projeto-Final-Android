package com.josevg.projetofinalapp.model.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "motores")
data class Motor(
    @PrimaryKey
    var modelo: String,
    var marca: String,
    var cilindradas: Float,
    var potencia: Int,
    var torque: Float,
    var combustivel: String,
    var status: String
)

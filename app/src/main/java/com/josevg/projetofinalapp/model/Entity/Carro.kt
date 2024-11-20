package com.josevg.projetofinalapp.model.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carros")
data class Carro(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var marca: String,
    var modelo: String,
    var ano: Int,
    var cor: String,
    var numPortas: Int,
    var motor: String,
    var status: String,
    var opcional1: String,
    var opcional2: String,
    var opcional3: String
)

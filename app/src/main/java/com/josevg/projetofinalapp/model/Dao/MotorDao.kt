package com.josevg.projetofinalapp.model.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.josevg.projetofinalapp.model.Entity.Motor

@Dao
interface MotorDao {

    @Insert
    suspend fun inserir(motor: Motor)

    @Query("SELECT * FROM motores")
    suspend fun buscarTodos(): List<Motor>

    @Query("SELECT modelo from motores")
    suspend fun buscarMotoresDisponiveis(): List<String>

    @Update
    suspend fun atualizarMotor(motor: Motor)

    @Delete
    suspend fun deletarMotor(motor: Motor)

}
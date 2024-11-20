package com.josevg.projetofinalapp.model.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.josevg.projetofinalapp.model.Entity.Carro


@Dao
interface CarroDao {

    @Insert
    suspend fun inserir(carro: Carro)

    @Query("SELECT * FROM carros")
    suspend fun buscarTodos(): List<Carro>

    @Update
    suspend fun atualizarCarro(carro: Carro)

    @Delete
    suspend fun deletarCarro(carro: Carro)

    @Query("SELECT * FROM carros WHERE status = 'Em produção'")
    suspend fun buscarProducao() : List<Carro>

    @Query("UPDATE carros SET status = 'Concluído' WHERE id = :carroId")
    suspend fun atualizarStatus(carroId: Int): Int

    @Query("SELECT * FROM carros WHERE status = 'Concluído'")
    suspend fun buscarCarrosConcluidos() : List<Carro>

}
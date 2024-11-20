package com.josevg.projetofinalapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.josevg.projetofinalapp.model.Dao.CarroDao
import com.josevg.projetofinalapp.model.Dao.MotorDao
import com.josevg.projetofinalapp.model.Entity.Carro
import com.josevg.projetofinalapp.model.Entity.Motor

@Database(entities = [Carro::class, Motor::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carroDao(): CarroDao
    abstract fun motorDao(): MotorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Use se for seguro perder dados antigos
                    .build()
                INSTANCE = instance
                instance
                }
            }
        }
    }

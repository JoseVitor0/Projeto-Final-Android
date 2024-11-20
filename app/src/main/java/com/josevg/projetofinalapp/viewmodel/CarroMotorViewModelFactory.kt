package com.josevg.projetofinalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josevg.projetofinalapp.model.Dao.CarroDao
import com.josevg.projetofinalapp.model.Dao.MotorDao

class CarroMotorViewModelFactory (
    private val carroDao: CarroDao,
    private val motorDao: MotorDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarroMotorViewModel::class.java)) {
                return CarroMotorViewModel(carroDao, motorDao) as T
            }
            throw IllegalArgumentException("Classe ViewModel desconhecida")
        }
    }

package com.josevg.projetofinalapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josevg.projetofinalapp.R
import com.josevg.projetofinalapp.model.Dao.CarroDao
import com.josevg.projetofinalapp.model.Dao.MotorDao
import com.josevg.projetofinalapp.model.Entity.Carro
import com.josevg.projetofinalapp.model.Entity.Motor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CarroMotorViewModel(private val carroDao: CarroDao, private val motorDao: MotorDao) : ViewModel() {

    var listaCarros = mutableStateOf(listOf<Carro>())
        private set

    var listaCarrosProducao = mutableStateOf(listOf<Carro>())
        private set

    var listaCarrosConcluidos = mutableStateOf(listOf<Carro>())
        private set

    var listaCarrosEdicao = mutableStateOf(listOf<Carro>())
        private set

    var listaMotores = mutableStateOf(listOf<Motor>())
        private set

    val listaMarcas = listOf("Toyota", "Volkswagen", "Chevrolet", "Nissan", "Honda", "BMW", "Porsche")

    val listaModelos = mapOf(
        "Toyota" to listOf("Corolla", "Camry", "RAV4", "Hilux", "Supra"),
        "Volkswagen" to listOf("Golf", "Passat", "Tiguan", "Jetta", "Polo"),
        "Chevrolet" to listOf("Onix", "Cruze", "Tracker", "S10", "Camaro"),
        "Nissan" to listOf("Versa", "Sentra", "Kicks", "Altima", "GT-R"),
        "Honda" to listOf("Civic", "Accord", "HR-V", "CR-V", "Fit"),
        "BMW" to listOf("320i", "X1", "X5", "Z4", "M3"),
        "Porsche" to listOf("911", "Cayenne", "Panamera", "Macan", "Taycan")
    )

    val mapDeImagens = mapOf(
        "Corolla" to R.drawable.corolla,
        "Camry" to R.drawable.camry,
        "RAV4" to R.drawable.rav4,
        "Hilux" to R.drawable.hilux,
        "Supra" to R.drawable.supra,
        "Golf" to R.drawable.golf,
        "Passat" to R.drawable.passat,
        "Tiguan" to R.drawable.tiguan,
        "Jetta" to R.drawable.jetta,
        "Polo" to R.drawable.polo,
        "Onix" to R.drawable.onix,
        "Cruze" to R.drawable.cruze,
        "Tracker" to R.drawable.tracker,
        "S10" to R.drawable.s10,
        "Camaro" to R.drawable.camaro,
        "Versa" to R.drawable.versa,
        "Sentra" to R.drawable.sentra,
        "Kicks" to R.drawable.kicks,
        "Altima" to R.drawable.altima,
        "GT-R" to R.drawable.gtr,
        "Civic" to R.drawable.civic,
        "Accord" to R.drawable.honda_accord,
        "HR-V" to R.drawable.honda_hrv,
        "CR-V" to R.drawable.honda_crv,
        "Fit" to R.drawable.fit,
        "320i" to R.drawable.bmw_320i,
        "X1" to R.drawable.x1,
        "X5" to R.drawable.x5,
        "Z4" to R.drawable.z4,
        "M3" to R.drawable.m3,
        "911" to R.drawable.porsche_911,
        "Cayenne" to R.drawable.cayenne,
        "Panamera" to R.drawable.panamera,
        "Macan" to R.drawable.macan,
        "Taycan" to R.drawable.taycan
    )

    val listaCombustiveis = listOf("Gasolina", "Alcool", "Diesel", "Flex", "Elétrico")
    val listaCores = listOf("Prata", "Branco", "Vermelho", "Preto", "Azul", "Cinza-chumbo", "Amarelo")
    val listaPortas = listOf("2", "4")


    private val _motoresDisponiveis = MutableStateFlow<List<String>>(emptyList())
    val motoresDisponiveis: StateFlow<List<String>> = _motoresDisponiveis

    init {
        carregarCarros()
        carregarMotores()
        carregarCarrosProducao()
        carregarCarrosConcluidos()
        carregarCarrosEdicao()
    }

    private val user = "master"
    private val password = "1234"

    fun login(usuario: String, senha: String) : String{
        if (usuario.isBlank() || senha.isBlank()){
            return "Preencha todos os campos!"
        }

        if (usuario != user || senha != password){
            return  "Usuário ou senha incorretos!"
        }

        return "Login feito com sucesso!"
    }



    private fun carregarCarros(){
        viewModelScope.launch {
            listaCarros.value = carroDao.buscarTodos()
        }
    }

    private fun carregarCarrosEdicao(){
        viewModelScope.launch{
            listaCarrosEdicao.value = carroDao.carrosEdicao()
        }
    }

    private fun carregarMotores(){
        viewModelScope.launch {
            val motores = motorDao.buscarTodos()

            listaMotores.value = motores

            _motoresDisponiveis.value = motores.map { "${it.marca} ${it.modelo}"}
        }
    }

    private fun carregarCarrosProducao(){
        viewModelScope.launch {
            listaCarrosProducao.value = carroDao.buscarProducao()
        }
    }

    private fun carregarCarrosConcluidos(){
        viewModelScope.launch {
            listaCarrosConcluidos.value = carroDao.buscarCarrosConcluidos()
        }
    }


    fun salvarCarro(marca: String, modelo: String, ano: String, cor: String, motor: String, numPortas: String, opcionais: List<String>) : String{
        if (marca.isBlank() || modelo.isBlank() || ano.isBlank() || numPortas.isBlank() || cor.isBlank()){
            return "Preencha todos os campos"
        }

        val opcionaisFormatados = (opcionais + List(3 - opcionais.size) { "Nenhum" }).take(3)

        val opcional1 = opcionaisFormatados[0]
        val opcional2 = opcionaisFormatados[1]
        val opcional3 = opcionaisFormatados[2]

        val motorModelo = motor.substringAfterLast(" ").trim()

        val carro = Carro(marca = marca, modelo =  modelo, ano =  ano.toInt(), cor = cor,
            numPortas =  numPortas.toInt(), motor =  motorModelo, status =  "Em produção",
            opcional1 = opcional1, opcional2 = opcional2, opcional3 = opcional3)

        viewModelScope.launch {
            carroDao.inserir(carro)
            carregarCarros()
            carregarCarrosProducao()
        }

        return "Carro $marca $modelo foi salvo com sucesso!"
    }

    fun excluirCarro(carro: Carro) : String{
        viewModelScope.launch {
            carroDao.deletarCarro(carro)
            carregarCarros()
            carregarCarrosProducao()
        }
        return "Carro Excluido com sucesso!"
    }

    fun atualizarCarro(id: Int, marca: String, modelo: String, ano: String, cor: String, numPortas: String, motor: String, opcionais: List<String>) : String{
        if (marca.isBlank() || modelo.isBlank()){
            return "Preencha todos os campos"
        }

        val opcionaisFormatados = (opcionais + List(3 - opcionais.size) { "Nenhum" }).take(3)

        val opcional1 = opcionaisFormatados[0]
        val opcional2 = opcionaisFormatados[1]
        val opcional3 = opcionaisFormatados[2]

        val status = "Em produção"
        val carro = listaCarros.value.find { it.id == id } ?: return "Erro ao atualizar o carro"
        val carroAtualizado = carro.copy(marca = marca, modelo = modelo, ano = ano.toInt(),
            cor = cor, numPortas = numPortas.toInt(), motor = motor, status = status,
            opcional1 = opcional1, opcional2 = opcional2, opcional3 = opcional3)

        viewModelScope.launch {
            carroDao.atualizarCarro(carroAtualizado)
            carregarCarros()
            carregarCarrosProducao()
        }

        return "Carro atualizado!"
    }

    fun salvarMotor(modelo: String, marca: String, cilindradas: String, potencia: String, torque: String, combustivel : String) : String{

        val status = "Em produção"
        if (modelo.isBlank() || marca.isBlank() || potencia.isBlank() || torque.isBlank() || combustivel.isBlank()){
            return "Preencha todos os parâmetros"
        }

        val motor = Motor(modelo, marca, cilindradas.toFloat(), potencia.toInt(), torque.toFloat(), combustivel, status)

        viewModelScope.launch{
            motorDao.inserir(motor)
            carregarMotores()
        }

        return "Motor ${marca} ${modelo} foi cadastrado com sucesso!"

    }

    fun atualizarStatus(carro: Carro, status: String){
        viewModelScope.launch {
            carroDao.atualizarStatus(carro.id, status)
            carregarCarros()
            carregarCarrosProducao()
            carregarCarrosConcluidos()
            carregarCarrosEdicao()
        }
    }

    fun buscarMotor(modelo: String): Motor? {

        return listaMotores.value.find { it.modelo == modelo }
    }

    fun buscarCarroPorId(id: Int): Carro?{

        return listaCarros.value.find { it.id == id }

    }


}
package com.josevg.projetofinalapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.josevg.projetofinalapp.R
import com.josevg.projetofinalapp.model.AppDatabase
import com.josevg.projetofinalapp.model.Entity.Carro
import com.josevg.projetofinalapp.viewmodel.CarroMotorViewModel
import com.josevg.projetofinalapp.viewmodel.CarroMotorViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {

    private val carroMotorViewModel: CarroMotorViewModel by viewModels {
        val carroDao = AppDatabase.getDatabase(applicationContext).carroDao()
        val motorDao = AppDatabase.getDatabase(applicationContext).motorDao()
        CarroMotorViewModelFactory(carroDao, motorDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(carroMotorViewModel)
        }
    }
}

@Composable
fun AppNavigation(carroMotorViewModel: CarroMotorViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login",
        ) {
            composable("login") { TelaLogin(carroMotorViewModel, navController) }
            composable("telaprincipal") { TelaPrincipal(navController, carroMotorViewModel) }
            composable("producao") { TelaProducao(navController = navController, carroMotorViewModel = carroMotorViewModel) }
        }
}

@Composable
fun TelaPrincipal(navController: NavController, carroMotorViewModel: CarroMotorViewModel) {
    val tabs = listOf("Motores", "Carros")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // TabLayout na parte superior
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        // Conteúdo das abas
        when (selectedTabIndex) {
            0 -> CadastroMotores(carroMotorViewModel, navController)
            1 -> CadastroCarros(carroMotorViewModel, navController)
        }
    }
}

@Composable
fun TelaLogin(carroMotorViewModel: CarroMotorViewModel, navController: NavController) {

    val context = LocalContext.current

    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), 
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        
        Text(text = "Login", fontSize = 30.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        TextField(value = usuario, onValueChange = {usuario = it}, label = {Text(text = "Usuário")}, modifier = Modifier.fillMaxWidth() )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = senha, onValueChange = {senha = it}, label = {Text(text = "Senha")},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val retorno = carroMotorViewModel.login(usuario, senha)
            Toast.makeText(context, retorno, Toast.LENGTH_SHORT).show()

            if (retorno == "Login feito com sucesso!"){
                navController.navigate("telaprincipal")
            }

        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Login")
        }
        
    }
}

@Composable
fun CadastroMotores(carroMotorViewModel: CarroMotorViewModel, navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        var marca by remember { mutableStateOf("") }
        var modelo by remember { mutableStateOf("") }
        var cilindradas by remember { mutableStateOf("") }
        var potencia by remember { mutableStateOf("") }
        var torque by remember { mutableStateOf("") }
        var combustivel by remember { mutableStateOf("") }

        val context = LocalContext.current


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center) {

            Text("Cadastro de motores", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Marca:")
            Spacer(Modifier.height(5.dp))
            marca = selectionBox(listaOpcoes = carroMotorViewModel.listaMarcas)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = modelo, onValueChange = {modelo = it},
                label = {Text(text = "Modelo")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = cilindradas, onValueChange = {cilindradas = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text(text = "Cilindradas (L)")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = potencia, onValueChange = {potencia = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text(text = "Potência (cv)")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = torque, onValueChange = {torque = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text(text = "Torque (kgfm)")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Tipo de combustível")
            combustivel =  selectionBox(listaOpcoes = carroMotorViewModel.listaCombustiveis)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                val retorno = carroMotorViewModel.salvarMotor(modelo, marca, cilindradas, potencia, torque, combustivel)
                Toast.makeText(context, retorno,Toast.LENGTH_SHORT).show()

                modelo = ""
                marca = ""
                potencia = ""
                torque = ""
                cilindradas = ""
                combustivel = ""
            },
                modifier = Modifier.fillMaxWidth()) {
                Text(text = "Cadastrar Motor")
            }

            Button(onClick = { navController.navigate("producao") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Produção")
            }

        }

    }
}

@Composable
fun CadastroCarros(carroMotorViewModel: CarroMotorViewModel, navController: NavController){

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var numPortas by remember { mutableStateOf("") }
    var cor by remember { mutableStateOf("") }
    var motor by remember { mutableStateOf("") }


    var motoresDisponives by remember { mutableStateOf(false) }

    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var listaCarros by carroMotorViewModel.listaCarros
    val selectedOptions = remember { mutableStateListOf<String>() }


    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
        ) {


            Text(text = "Cadastrar Carros", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Marca:")
            Spacer(Modifier.height(5.dp))
            marca = selectionBox(listaOpcoes = carroMotorViewModel.listaMarcas)

            Spacer(modifier = Modifier.height(16.dp))

            if(marca.isNotBlank()){
                Text(text = "Modelo:")
                Spacer(Modifier.height(5.dp))
                modelo =
                    carroMotorViewModel.listaModelos[marca]?.let { selectionBox(listaOpcoes = it) }.toString()

                if (modelo.isNotBlank()){
                    val imagemRes = carroMotorViewModel.mapDeImagens[modelo] ?: R.drawable.placeholder
                    Image(
                        painter = painterResource(id = imagemRes),
                        contentDescription = "car",
                        modifier = Modifier
                            .size(200.dp)
                            .fillMaxWidth()
                    )
                }

            }


            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = ano, onValueChange = {ano = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {Text(text = "Ano")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Cor:")
            Spacer(Modifier.height(5.dp))
            cor = selectionBox(listaOpcoes = carroMotorViewModel.listaCores)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Número de portas:")
            Spacer(Modifier.height(5.dp))
            numPortas = selectionBox(listaOpcoes = carroMotorViewModel.listaPortas)

            Spacer(modifier = Modifier.height(16.dp))


            val listaMotores by carroMotorViewModel.motoresDisponiveis.collectAsState()
            if (listaMotores.isNotEmpty()){
                motor = selectionBox(listaOpcoes = listaMotores)
                motoresDisponives = true
            } else {
                Text(text = "Não existem motores disponíveis! Não é possível produzir um carro no momento!")
            }


            Spacer(modifier = Modifier.height(16.dp))


        var checked by remember { mutableStateOf(false) }

        Text(text = "Deseja acresentar opcionais ao veículo?")
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )


        if(checked){
            val options = listOf("Teto Solar", "Bancos de couro", "Cor exclusiva", "Piloto automático", "Sistema de manutenção em faixa")
            var checkedStates = remember { mutableStateListOf(false, false, false, false, false) }
            var checkCount by remember { mutableIntStateOf(0) }

            Column {
                options.forEachIndexed { index, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { isChecked ->
                                if (isChecked && checkCount >= 3) {
                                    // Limite atingido, não permite marcar mais
                                    Toast.makeText(
                                        context,
                                        "Quantidade máxima de opções selecionadas!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Atualiza estado e conta de selecionados
                                    checkedStates[index] = isChecked
                                    checkCount += if (isChecked) 1 else -1
                                }

                                if (isChecked) {
                                    selectedOptions.add(option)
                                } else {
                                    selectedOptions.remove(option)
                                }
                            }
                        )
                        Text(
                            text = option,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

            Button(onClick = {
                val retorno = carroMotorViewModel.salvarCarro(marca, modelo, ano, cor, motor, numPortas, selectedOptions)
                Toast.makeText(context, retorno,Toast.LENGTH_SHORT).show()

                marca = ""
                modelo = ""
                ano = ""
                cor = ""
                motor = ""
                numPortas = ""


            }, modifier = Modifier.fillMaxWidth(), enabled = motoresDisponives) {
                Text(text = "Cadastrar Carro")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("producao") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Produção")
            }

        }

}


@Composable
fun TelaProducao(navController: NavController, carroMotorViewModel: CarroMotorViewModel){
    val tabs = listOf("Produção", "Concluídos")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // TabLayout na parte superior
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        // Conteúdo das abas
        when (selectedTabIndex) {
            0 -> TelaEmProducao(carroMotorViewModel)
            1 -> TelaConcluidos(carroMotorViewModel)
        }
    }
}

@Composable
fun TelaEmProducao(carroMotorViewModel: CarroMotorViewModel){

    var listaCarros by carroMotorViewModel.listaCarrosProducao

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center
        ) {

        if (listaCarros.isEmpty()) {
            Text("Nenhum carro em produção.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(listaCarros) { carro ->
                    CarroItem(carro = carro, carroMotorViewModel)


                }
            }
        }



    }
}

@Composable
fun CarroItem(carro: Carro, carroMotorViewModel: CarroMotorViewModel) {
    var currentProgress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val imagemRes = carroMotorViewModel.mapDeImagens[carro.modelo] ?: R.drawable.placeholder

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(Modifier.padding(8.dp)) {
                Column(Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = imagemRes),
                        contentDescription = "car",
                        modifier = Modifier
                            .size(200.dp)
                            .fillMaxWidth()
                    )

                    Text(text = "Marca: ${carro.marca}")
                    Text(text = "Modelo: ${carro.modelo}")
                    Text(text = "Ano: ${carro.ano}")
                    Spacer(modifier = Modifier.height(8.dp))
                    val listaOpcionais = listOf(carro.opcional1, carro.opcional2, carro.opcional3)
                    for (opcional in listaOpcionais) {
                        if (opcional != "Nenhum") {
                            Text(text = "Opcionais: ${opcional}")
                        }
                    }
                    Text(text = "Status: ${carro.status}")

                    Spacer(modifier = Modifier.height(8.dp))

                    if (carro.status == "Em produção") {
                        Button(
                            onClick = {
                                loading = true
                                scope.launch {
                                    loadProgress { progress ->
                                        currentProgress = progress
                                    }
                                    loading = false
                                    carroMotorViewModel.atualizarStatus(carro)
                                    Toast.makeText(
                                        context,
                                        "Carro ${carro.marca} ${carro.modelo} produzido com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            enabled = !loading
                        ) {
                            Text("Produzir")
                        }

                        if (loading) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                progress = currentProgress
                            )
                        }
                    }
                }
            }
        }
    }
}




suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}


@Composable
fun TelaConcluidos(carroMotorViewModel: CarroMotorViewModel){

    var listaCarros by carroMotorViewModel.listaCarrosConcluidos

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        if (listaCarros.isEmpty()) {
            Text("Nenhum carro concluido.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(listaCarros) { carro ->
                    CarroItem(carro = carro, carroMotorViewModel)
                }
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun selectionBox(
    listaOpcoes: List<String>
) : String{
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // Campo de seleção (TextField)
        TextField(
            value = selectedOption,
            onValueChange = { selectedOption = it },
            label = { Text("Selecione uma opção") },
            modifier = Modifier
                .menuAnchor() // Faz com que o menu se alinhe ao TextField
                .fillMaxWidth(),
            readOnly = true, // Deixa o campo apenas para seleção, sem edição direta pelo usuário
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // Menu suspenso com as opções da lista
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listaOpcoes.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }

    return selectedOption
}

@Preview(showBackground = true)
@Composable
fun LayoutPreview() {

}
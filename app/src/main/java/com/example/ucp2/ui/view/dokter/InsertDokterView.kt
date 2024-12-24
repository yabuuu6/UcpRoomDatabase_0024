package com.example.ucp2.ui.view.dokter


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.data.entity.Spesialis
import com.example.ucp2.ui.costumwidget.DynamicSelected
import com.example.ucp2.ui.costumwidget.TopAppBar
import com.example.ucp2.ui.navigation.AlamatNavigasi
import com.example.ucp2.ui.viewmodel.DokterEvent
import com.example.ucp2.ui.viewmodel.DokterUIState
import com.example.ucp2.ui.viewmodel.DokterViewModel
import com.example.ucp2.ui.viewmodel.FormErrorState
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@Composable
fun FormDokter(
    dokterEvent: DokterEvent = DokterEvent(),
    onValueChange: (DokterEvent) -> Unit = { },
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
){

    Column (
        modifier = modifier.fillMaxWidth()
    ){
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = dokterEvent.nama,
            onValueChange = {
                onValueChange(dokterEvent.copy(nama = it))
            },
            label = { Text(text = "Nama Dokter") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            isError = errorState.nama != null,
            placeholder = { Text(text = "Masukkan Nama Dokter")}
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )

        DynamicSelected(
            selectedValue = dokterEvent.spesialis,
            options = Spesialis.option,
            label = "Spesialis",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null
                )
            },
            onValueChangedEvent = {
                onValueChange(dokterEvent.copy(spesialis = it))
            }
        )

        Spacer(modifier = Modifier.size(20.dp))

        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = dokterEvent.klinik,
            onValueChange = {
                onValueChange(dokterEvent.copy(klinik = it))
            },
            label = { Text(text = "Nama Klinik") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            isError = errorState.klinik != null,
            placeholder = { Text(text = "Masukkan Nama Klinik")}
        )
        Text(
            text = errorState.klinik ?: "",
            color = Color.Red
        )

        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = dokterEvent.telepon,
            onValueChange = {
                onValueChange(dokterEvent.copy(telepon = it))
            },
            label = { Text(text = "No Hp") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null
                )
            },
            isError = errorState.telepon != null,
            placeholder = { Text(text = "Masukkan No Hp")}
        )
        Text(
            text = errorState.telepon ?: "",
            color = Color.Red
        )

        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = dokterEvent.jamKerja,
            onValueChange = {
                onValueChange(dokterEvent.copy(jamKerja = it))
            },
            label = { Text(text = "Jam Kerja") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            },
            isError = errorState.nama != null,
            placeholder = { Text(text = "Masukkan Jam Kerja")}
        )
        Text(
            text = errorState.jamKerja ?: "",
            color = Color.Red
        )
    }
}

@Composable
fun InsertBodyDokter(
    modifier: Modifier = Modifier,
    onValueChange: (DokterEvent) -> Unit,
    uiState: DokterUIState,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormDokter(
            dokterEvent = uiState.dokterEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = modifier.fillMaxWidth()
        )
        Button(
            onClick = { onSaveClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}


object DestinasiInsertDokter : AlamatNavigasi {
    override val route = "insert_dokter"
}

@Composable
fun InsertDokterView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DokterViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect (uiState.snackbarMessage){
        uiState.snackbarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackbarMessage()
            }
        }
    }

    Scaffold (
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Tambah Dokter"
            )
        },
    ){
            padding ->
        Column (
            modifier = modifier.fillMaxWidth()
                .padding(padding)
                .padding(16.dp)
        ){
            InsertBodyDokter(
                uiState = uiState,
                onValueChange = {updatedEvent ->
                    viewModel.updateState(updatedEvent)
                },
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.saveDokter()
                    }
                    onNavigate()
                }
            )
        }
    }
}
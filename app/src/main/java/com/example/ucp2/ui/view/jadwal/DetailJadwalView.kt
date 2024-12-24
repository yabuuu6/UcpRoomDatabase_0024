package com.example.ucp2.ui.view.jadwal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.ui.costumwidget.TopAppBar
import com.example.ucp2.ui.viewmodel.DetailJadwalUiState
import com.example.ucp2.ui.viewmodel.DetailJadwalViewModel
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import com.example.ucp2.ui.viewmodel.toJadwalEntity

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = "Delete Data") },
        text = { Text(text = "Apakah Anda Yakin Ingin Menghapus Data Ini?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Batal")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Hapus")
            }
        }
    )
}

@Composable
fun ComponentDetailJadwal(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String
) {
    Column (
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = "$judul:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ItemDetailJadwal(
    modifier: Modifier = Modifier,
    jadwal: Jadwal
) {
    Card( modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF9861FF),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer )
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            ComponentDetailJadwal(judul = "Nama Pasien", isinya = jadwal.namaPasien)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailJadwal(judul = "Nama Dokter", isinya = jadwal.namaDokter)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailJadwal(judul = "Telepon", isinya = jadwal.telepon)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailJadwal(judul = "Tanggal", isinya = jadwal.tanggal)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailJadwal(judul = "Status", isinya = jadwal.status)
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun BodyDetailJadwal(
    modifier: Modifier = Modifier,
    detailJadwalUiState: DetailJadwalUiState = DetailJadwalUiState(),
    onDeleteClick: () -> Unit = {  }
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when {
        detailJadwalUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        detailJadwalUiState.isUiJadwalEventNotEmpty -> {
            Column (
                modifier = modifier.fillMaxSize()
                    .padding(16.dp)
            ){
                ItemDetailJadwal(
                    jadwal = detailJadwalUiState.detailJadwalUiEvent.toJadwalEntity(),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = { deleteConfirmationRequired = true },
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text = "Delete")
                }

                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick()
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier
                    )
                }
            }
        }

        detailJadwalUiState.isUiJadwalEventEmpty -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Data Tidak Ditemukan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun DetailJadwalView(
    modifier: Modifier = Modifier,
    viewModel: DetailJadwalViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit = {  },
    onEditClick: (String) -> Unit = {  },
    onDeleteClick: () -> Unit = {  }
) {
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                judul = "Detail Jadwal",
                showBackButton = true,
                onBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEditClick(viewModel.detailJadwalUiState.value.detailJadwalUiEvent.id.toString()) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Jadwal"
                )
            }
        }
    ){
            innerPadding ->
        val detailJadwalUiState by viewModel.detailJadwalUiState.collectAsState()

        BodyDetailJadwal(
            modifier = modifier.padding(innerPadding),
            detailJadwalUiState = detailJadwalUiState,
            onDeleteClick = {
                viewModel.deleteJadwal()
                onDeleteClick()
            }
        )
    }
}
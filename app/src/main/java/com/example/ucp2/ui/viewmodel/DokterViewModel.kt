package com.example.ucp2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.data.repository.RepositoryDokter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


object idDok {
    private var currentId = 0
    fun auto(): Int {
        currentId += 1
        return currentId
    }
}

data class DokterEvent(
    val id: Int = idDok.auto(),
    val nama: String = "",
    val spesialis: String = "",
    val klinik: String = "",
    val telepon: String = "",
    val jamKerja: String = ""
)

fun DokterEvent.toDokterEntity(): Dokter = Dokter (
    id = id,
    nama = nama,
    spesialis = spesialis,
    klinik = klinik,
    telepon = telepon,
    jamKerja = jamKerja
)

data class FormErrorState(
    val nama: String? = null,
    val spesialis: String? = null,
    val klinik: String? = null,
    val telepon: String? = null,
    val jamKerja: String? = null
) {
    fun isValid(): Boolean {
        return nama == null && spesialis == null &&
                klinik == null && telepon == null && jamKerja == null
    }
}

data class DokterUIState(
    val dokterEvent: DokterEvent = DokterEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackbarMessage: String? = null
)

class DokterViewModel(
    private val repositoryDokter: RepositoryDokter
): ViewModel() {

    private val _dokterNames = MutableStateFlow<List<String>>(emptyList())
    val dokterNames: StateFlow<List<String>> = _dokterNames

    init {
        viewModelScope.launch {
            repositoryDokter.getAllDokter()
                .collect { dokterList ->
                    _dokterNames.value = dokterList.map { it.nama }
                }
        }
    }

    var uiState: DokterUIState by mutableStateOf(DokterUIState())

    fun updateState(dokterEvent: DokterEvent) {
        uiState = uiState.copy(
            dokterEvent = dokterEvent
        )
    }

    private fun validateInput(): Boolean  {
        val event = uiState.dokterEvent
        val errorState = FormErrorState(
            nama = if (event.nama.isEmpty()) "Nama Tidak Boleh Kosong" else null,
            spesialis = if (event.spesialis.isEmpty()) "Spesialis Tidak Boleh Kosong" else null,
            klinik = if (event.klinik.isEmpty()) "Klinik Tidak Boleh Kosong" else null,
            telepon = if (event.telepon.isEmpty()) "Telepon Tidak Boleh Kosong" else null,
            jamKerja = if (event.jamKerja.isEmpty()) "Jam Kerja Tidak Boleh Kosong" else null
        )

        uiState = uiState.copy(
            isEntryValid = errorState
        )

        return errorState.isValid()
    }

    fun saveDokter() {
        val currentDokterEvent = uiState.dokterEvent

        if (validateInput()) {
            viewModelScope.launch {
                try {
                    repositoryDokter.insertDokter(currentDokterEvent.toDokterEntity())
                    uiState = uiState.copy(
                        dokterEvent = DokterEvent(),
                        isEntryValid = FormErrorState(),
                        snackbarMessage = "Dokter Berhasil Disimpan"
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackbarMessage = "Dokter Gagal Disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackbarMessage = "Input Tidak Valid. Periksa Kembali Data Anda"
            )
        }
    }

    fun resetSnackbarMessage() {
        uiState = uiState.copy(
            snackbarMessage = null
        )
    }
}
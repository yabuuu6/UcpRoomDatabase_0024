package com.example.ucp2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.data.repository.RepositoryJadwal
import kotlinx.coroutines.launch

object idjadwal {
    private var currentId = 0
    fun auto(): Int {
        currentId += 1
        return currentId
    }
}

data class JadwalEvent(
    val id: Int = idjadwal.auto(),
    val namaDokter: String = "",
    val namaPasien: String= "",
    val telepon: String = "",
    val tanggal: String = "",
    val status: String = "",
)

fun JadwalEvent.toJadwalEntity(): Jadwal = Jadwal(
    id = id,
    namaDokter = namaDokter,
    namaPasien = namaPasien,
    telepon = telepon,
    tanggal = tanggal,
    status = status
)

data class FormJadwalErrorState(
    val namaDokter: String? = null,
    val namaPasien: String? = null,
    val telepon: String? = null,
    val tanggal: String? = null,
    val status: String? = null
) {
    fun isValid(): Boolean {
        return namaDokter == null && namaPasien == null &&
                telepon == null && tanggal == null && status == null
    }
}

data class JadwalUIState(
    val jadwalEvent: JadwalEvent = JadwalEvent(),
    val isEntryValid: FormJadwalErrorState = FormJadwalErrorState(),
    val snackbarMessage: String? = null
)

class JadwalViewModel(
    private val repositoryJadwal: RepositoryJadwal
) : ViewModel() {

    var uiState: JadwalUIState by mutableStateOf(JadwalUIState())

    fun updateState(jadwalEvent: JadwalEvent) {
        uiState = uiState.copy(
            jadwalEvent = jadwalEvent
        )
    }

    private fun validateInput(): Boolean {
        val event = uiState.jadwalEvent
        val errorState = FormJadwalErrorState(
            namaDokter = if (event.namaDokter.isEmpty()) "Nama Dokter Tidak Boleh Kosong" else null,
            namaPasien = if (event.namaPasien.isEmpty()) "Nama Pasien Tidak Boleh Kosong" else null,
            telepon = if (event.telepon.isEmpty()) "Telepon Tidak Boleh Kosong" else null,
            tanggal = if (event.tanggal.isEmpty()) "Tanggal Tidak Boleh Kosong" else null,
            status = if (event.status.isEmpty()) "Status Tidak Boleh Kosong" else null
        )

        uiState = uiState.copy(
            isEntryValid = errorState
        )

        return errorState.isValid()
    }

    fun saveJadwal() {
        val currentJadwalEvent = uiState.jadwalEvent

        if (validateInput()) {
            viewModelScope.launch {
                try {
                    repositoryJadwal.insertJadwal(currentJadwalEvent.toJadwalEntity())
                    uiState = uiState.copy(
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormJadwalErrorState(),
                        snackbarMessage = "Jadwal Berhasil Disimpan"
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackbarMessage = "Jadwal Gagal Disimpan"
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
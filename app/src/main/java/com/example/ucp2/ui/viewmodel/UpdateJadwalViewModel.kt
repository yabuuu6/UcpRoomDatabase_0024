package com.example.ucp2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.data.repository.RepositoryJadwal
import com.example.ucp2.ui.navigation.DestinasiEditJadwal
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateJadwalViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryJadwal: RepositoryJadwal
): ViewModel() {

    var updateJadwalUIState by mutableStateOf(JadwalUIState())
        private set

    private val _id: Int = checkNotNull(savedStateHandle[DestinasiEditJadwal.ID])

    init {
        viewModelScope.launch {
            updateJadwalUIState = repositoryJadwal.getJadwal(_id)
                .filterNotNull()
                .first()
                .toUIStateJadwal()
        }
    }

    fun updateJadwal(jadwalEvent: JadwalEvent) {
        updateJadwalUIState = updateJadwalUIState.copy(
            jadwalEvent = jadwalEvent
        )
    }

    fun validateInput(): Boolean {
        val event = updateJadwalUIState.jadwalEvent
        val errorState = FormJadwalErrorState(
            namaPasien = if (event.namaPasien.isNotEmpty()) null else "Nama pasien tidak boleh kosong",
            namaDokter = if (event.namaDokter.isNotEmpty()) null else "Nama dokter tidak boleh kosong",
            telepon = if (event.telepon.isNotEmpty()) null else "Nomor telepon tidak boleh kosong",
            tanggal = if (event.tanggal.isNotEmpty()) null else "Tanggal tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong"
        )
        updateJadwalUIState = updateJadwalUIState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun updateJadwal() {
        val currentJadwal = updateJadwalUIState.jadwalEvent

        if (validateInput()) {
            viewModelScope.launch {
                try {
                    repositoryJadwal.updateJadwal(currentJadwal.toJadwalEntity())
                    updateJadwalUIState = updateJadwalUIState.copy(
                        snackbarMessage = "Jadwal Berhasil Di Update",
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormJadwalErrorState()
                    )
                    println("snackBarMessage Diatur: ${updateJadwalUIState.snackbarMessage}")
                } catch (e: Exception) {
                    updateJadwalUIState = updateJadwalUIState.copy(
                        snackbarMessage = "Jadwal Gagal Diupdate"
                    )
                }
            }
        } else {
            updateJadwalUIState = updateJadwalUIState.copy(
                snackbarMessage = "Jadwal Gagal Diupdate"
            )
        }
    }

    fun resetSnackbarMessage() {
        updateJadwalUIState = updateJadwalUIState.copy(
            snackbarMessage = null
        )
    }
}

fun Jadwal.toUIStateJadwal(): JadwalUIState = JadwalUIState(
    jadwalEvent = this.toDetailJadwalEvent()
)
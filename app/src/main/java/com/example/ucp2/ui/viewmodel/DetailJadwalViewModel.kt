package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.data.repository.RepositoryJadwal
import com.example.ucp2.ui.navigation.DestinasiDetailJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetailJadwalUiState(
    val detailJadwalUiEvent: JadwalEvent = JadwalEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
) {
    val isUiJadwalEventEmpty: Boolean
        get() = detailJadwalUiEvent == JadwalEvent()

    val isUiJadwalEventNotEmpty: Boolean
        get() = detailJadwalUiEvent != JadwalEvent()
}

fun Jadwal.toDetailJadwalEvent(): JadwalEvent {
    return JadwalEvent(
        id = id,
        namaPasien = namaPasien,
        namaDokter = namaDokter,
        telepon = telepon,
        tanggal = tanggal,
        status = status
    )
}

class DetailJadwalViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryJadwal: RepositoryJadwal
): ViewModel() {

    private val _id: Int = checkNotNull(savedStateHandle[DestinasiDetailJadwal.ID])

    val detailJadwalUiState : StateFlow<DetailJadwalUiState> = repositoryJadwal.getJadwal(_id)
        .filterNotNull()
        .map {
            DetailJadwalUiState(
                detailJadwalUiEvent = it.toDetailJadwalEvent(),
                isLoading = false
            )
        }
        .onStart {
            emit(DetailJadwalUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailJadwalUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailJadwalUiState(isLoading = true)
        )

    fun deleteJadwal() {
        detailJadwalUiState.value.detailJadwalUiEvent.toJadwalEntity().let {
            viewModelScope.launch {
                repositoryJadwal.deleteJadwal(it)
            }
        }
    }
}
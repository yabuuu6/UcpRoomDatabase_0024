package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.data.repository.RepositoryJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

data class JadwalHomeUiState(
    val jadwalList: List<Jadwal> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
)

data class JadwalAppViewModel(
    private val jadwalRepository: RepositoryJadwal
): ViewModel() {

    val jadwalUiState: StateFlow<JadwalHomeUiState> = jadwalRepository.getAllJadwal()
        .filterNotNull()
        .map {
            JadwalHomeUiState(
                jadwalList = it.toList(),
                isLoading = false
            )
        }
        .onStart {
            emit(JadwalHomeUiState(isLoading = true))
            delay(900)
        }
        .catch {
            emit(
                JadwalHomeUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = JadwalHomeUiState(
                isLoading = true
            )
        )
}
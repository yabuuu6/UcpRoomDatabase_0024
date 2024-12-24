package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2.myApp

object PenyediaViewModel  {

    val Factory = viewModelFactory {
        initializer {
            DokterViewModel(
                App().containerApp.repositoryDokter
            )
        }
        initializer {
            HomeAppViewModel(
                App().containerApp.repositoryDokter
            )
        }
        initializer {
            JadwalViewModel(
                App().containerApp.repositoryJadwal
            )
        }
        initializer {
            JadwalAppViewModel(
                App().containerApp.repositoryJadwal
            )
        }

        initializer {
            DetailJadwalViewModel(
                createSavedStateHandle(),
                App().containerApp.repositoryJadwal
            )
        }

        initializer {
            UpdateJadwalViewModel(
                createSavedStateHandle(),
                App().containerApp.repositoryJadwal
            )
        }
    }
}

fun CreationExtras.App(): myApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as myApp)
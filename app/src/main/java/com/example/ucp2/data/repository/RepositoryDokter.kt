package com.example.ucp2.data.repository

import com.example.ucp2.data.entity.Dokter
import kotlinx.coroutines.flow.Flow


interface RepositoryDokter {
    suspend fun insertDokter(dokter: Dokter)
    fun getAllDokter(): Flow<List<Dokter>>
    fun getDokter(id: Int): Flow<Dokter?>
}


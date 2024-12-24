package com.example.ucp2.data.repository

import com.example.ucp2.data.dao.JadwalDao
import com.example.ucp2.data.entity.Jadwal
import kotlinx.coroutines.flow.Flow

class LocalRepositoryJadwal (
    private val jadwalDao: JadwalDao
): RepositoryJadwal {

    override suspend fun insertJadwal(jadwal: Jadwal) {
        jadwalDao.insertJadwal(jadwal)
    }

    override suspend fun updateJadwal(jadwal: Jadwal) {
        jadwalDao.updateJadwal(jadwal)
    }

    override suspend fun deleteJadwal(jadwal: Jadwal) {
        jadwalDao.deleteJadwal(jadwal)
    }

    override fun getAllJadwal(): Flow<List<Jadwal>> {
        return jadwalDao.getAllJadwal()
    }

    override fun getJadwal(id: Int): Flow<Jadwal?> {
        return jadwalDao.getJadwalById(id)
    }
}



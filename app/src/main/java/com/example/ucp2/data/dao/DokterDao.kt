package com.example.ucp2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ucp2.data.entity.Dokter

@Dao
interface DokterDao{
    @Insert
    suspend fun insertDokter(dokter: Dokter): Long;

    @Query("SELECT * FROM dokter")
    suspend fun getAllDokters(): List<Dokter>;

    @Query("SELECT * FROM dokter WHERE id = :id")
    suspend fun getDokterById(id: Int): Dokter?;

}



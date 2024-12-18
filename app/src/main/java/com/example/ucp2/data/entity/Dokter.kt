package com.example.ucp2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dokter")
data class Dokter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val Nama: String,
    val Spesialis: String,
    val Klinik: String,
    val NoTelpn: String,
    val JamKerja: String
)

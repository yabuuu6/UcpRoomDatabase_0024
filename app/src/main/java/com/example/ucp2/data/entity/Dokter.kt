package com.example.ucp2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dokter")
data class Dokter (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val spesialis: String,
    val klinik: String,
    val telepon: String,
    val jamKerja: String
)
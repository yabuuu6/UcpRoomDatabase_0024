package com.example.ucp2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jadwal")

data class Jadwal (
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val namaDokter: String,
        val namaPasien: String,
        val telepon: String,
        val tanggal: String,
        val status: String
)

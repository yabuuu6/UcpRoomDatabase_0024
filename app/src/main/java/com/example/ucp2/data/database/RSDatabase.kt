package com.example.ucp2.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.ucp2.data.dao.DokterDao
import com.example.ucp2.data.dao.JadwalDao
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.data.entity.Jadwal

@Database(entities = [Dokter::class, Jadwal::class], version = 1)
abstract class RSDatabase : RoomDatabase() {

    abstract fun dokterDao(): DokterDao
    abstract fun jadwalDao(): JadwalDao

    companion object {
        @Volatile
        private var INSTANCE: RSDatabase? = null

        fun getDatabase(context: Context): RSDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RSDatabase::class.java,
                    "rsdatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


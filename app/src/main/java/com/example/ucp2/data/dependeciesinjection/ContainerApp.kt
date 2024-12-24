package com.example.ucp2.data.dependeciesinjection

import android.content.Context
import com.example.ucp2.data.database.RSDatabase
import com.example.ucp2.data.repository.LocalRepositoryDokter
import com.example.ucp2.data.repository.LocalRepositoryJadwal
import com.example.ucp2.data.repository.RepositoryDokter
import com.example.ucp2.data.repository.RepositoryJadwal


interface InterfaceContainerApp{
    val repositoryDokter: RepositoryDokter
    val repositoryJadwal: RepositoryJadwal

}

class ContainerApp(private val context: Context) : InterfaceContainerApp {
    override val repositoryDokter: RepositoryDokter by lazy {
        LocalRepositoryDokter(RSDatabase.getDatabase(context).dokterDao())
    }
    override val repositoryJadwal: RepositoryJadwal by lazy {
        LocalRepositoryJadwal(RSDatabase.getDatabase(context).jadwalDao())
    }
}

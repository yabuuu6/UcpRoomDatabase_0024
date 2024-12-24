package com.example.ucp2.ui.navigation


interface AlamatNavigasi {
    val route: String
}

object DestinasiHome : AlamatNavigasi {
    override val route = "home"
}


object DestinasiJadwal : AlamatNavigasi {
    override val route = "jadwal"
}

object DestinasiDetailJadwal : AlamatNavigasi {
    override val route = "detailJadwal"
    const val ID = "id"
    val routeWithArgs = "$route/{$ID}"
}

object DestinasiEditJadwal : AlamatNavigasi {
    override val route = "editJadwal"
    const val ID = "id"
    val routeWithArgs = "$route/{$ID}"
}
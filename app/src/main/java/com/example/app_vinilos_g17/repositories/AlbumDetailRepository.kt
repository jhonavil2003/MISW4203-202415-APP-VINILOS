package com.example.app_vinilos_g17.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.network.NetworkServiceAdapter

class AlbumDetailRepository(val application: Application) {
    private val networkService = NetworkServiceAdapter.getInstance(application)

    suspend fun getAlbumDetail(albumId: Int): Album {
        // Llamada a la función suspending de NetworkServiceAdapter
        return networkService.getAlbumDetail(albumId)
    }
}

package com.example.app_vinilos_g17.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.network.NetworkServiceAdapter

class AlbumDetailRepository(val application: Application) {
    private val networkService = NetworkServiceAdapter.getInstance(application)

    fun getAlbumDetail(albumId: Int, onComplete: (Album) -> Unit, onError: (VolleyError) -> Unit) {
        networkService.getAlbumDetail(albumId, onComplete, onError)
    }
}

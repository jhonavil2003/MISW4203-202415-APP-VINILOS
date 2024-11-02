package com.example.app_vinilos_g17.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.network.NetworkServiceAdapter

class AlbumListRepository(val application: Application) {
    private val networkService = NetworkServiceAdapter.getInstance(application)

    fun refreshData(onComplete: (List<Album>) -> Unit, onError: (VolleyError) -> Unit) {
        networkService.getAlbumList(onComplete, onError)
    }
}

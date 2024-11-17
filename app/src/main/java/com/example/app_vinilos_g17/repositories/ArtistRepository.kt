package com.example.app_vinilos_g17.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.app_vinilos_g17.models.Artist
import com.example.app_vinilos_g17.network.NetworkServiceAdapter

class ArtistRepository(val application: Application) {
    private val networkService = NetworkServiceAdapter.getInstance(application)

    fun refreshData(onComplete: (List<Artist>) -> Unit, onError: (VolleyError) -> Unit) {
        networkService.getArtists(onComplete, onError)
    }
}
package com.example.app_vinilos_g17.repositories

import android.app.Application
import android.util.Log
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.models.Artist
import com.example.app_vinilos_g17.network.NetworkServiceAdapter
import com.example.app_vinilos_g17.network.CacheManager

class ArtistRepository(val application: Application) {
    private val networkService = NetworkServiceAdapter.getInstance(application)
    private val cacheManager = CacheManager.getInstance(application.applicationContext)

    companion object {
        private const val TAG = "ArtistListRepository"
    }

    suspend fun getArtists(): List<Artist> {
        val cachedArtistList = cacheManager.getArtists()
        if (cachedArtistList != null) {
            Log.d(TAG, "Se retorna información desde caché")
            return cachedArtistList
        } else {
            Log.d(TAG, "Se retorna información desde API")
            val artistList = networkService.getArtists()
            cacheManager.addArtists(artistList)
            return artistList
        }
    }

    suspend fun getArtistDetail(artistId: Int): Artist {
        val cachedArtist = cacheManager.getArtistDetail(artistId)

        if (cachedArtist != null) {
            Log.d("Cache decision", "Se retorna información desde caché")
            return cachedArtist
        } else {
            Log.d("Cache decision", "Se retorna información desde api")
            val artist = networkService.getArtistDetail(artistId)
            cacheManager.addArtistDetail(artistId, artist)

            return artist
        }
    }
}
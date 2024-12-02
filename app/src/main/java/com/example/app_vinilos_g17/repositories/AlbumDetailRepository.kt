package com.example.app_vinilos_g17.repositories

import android.app.Application
import android.util.Log
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.network.CacheManager
import com.example.app_vinilos_g17.network.NetworkServiceAdapter

class AlbumDetailRepository(val application: Application) {
    private val networkService = NetworkServiceAdapter.getInstance(application)

    private val cacheManager = CacheManager.getInstance(application.applicationContext)

    suspend fun getAlbumDetail(albumId: Int): Album {
        val cachedAlbum = cacheManager.getAlbumDetails(albumId)

        if (cachedAlbum != null) {
            Log.d("Cache decision", "Se retorna información desde caché")
            return cachedAlbum
        } else {
            Log.d("Cache decision", "Se retorna información desde api")
            val album = networkService.getAlbumDetail(albumId)
            cacheManager.addAlbumDetails(albumId, album)

            return album
        }
    }

    suspend fun setTrackAlbum(albumId: Int, album: Map<String, String>): String {
        try {
            val albumCreated = networkService.setTrack(albumId, album)
            Log.d("Track", "Album creado con éxito")
            return albumCreated
        } catch (e: Exception) {
            Log.e("Track", "Error creando album", e)
            throw e
        }
    }
}

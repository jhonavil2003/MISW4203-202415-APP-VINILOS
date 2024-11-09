package com.example.app_vinilos_g17.repositories

import android.app.Application
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.network.NetworkServiceAdapter
import com.example.app_vinilos_g17.network.CacheManager
import android.util.Log

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
}

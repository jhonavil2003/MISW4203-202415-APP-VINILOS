package com.example.app_vinilos_g17.repositories

import android.app.Application
import android.util.Log
import com.example.app_vinilos_g17.models.AlbumList
import com.example.app_vinilos_g17.network.CacheManager
import com.example.app_vinilos_g17.network.NetworkServiceAdapter

class AlbumListRepository(val application: Application) {
    private val networkService = NetworkServiceAdapter.getInstance(application)
    private val cacheManager = CacheManager.getInstance(application.applicationContext)

    companion object {
        private const val TAG = "AlbumListRepository"
    }

    suspend fun getAlbumList(forceNetworkRefresh: Boolean = false): List<AlbumList> {
        val cachedAlbumList = cacheManager.getAlbumList()
        if (cachedAlbumList != null && !forceNetworkRefresh) {
            Log.d(TAG, "Se retorna información desde caché")
            return cachedAlbumList
        } else {
            Log.d(TAG, "Se retorna información desde el API")
            val albumList = networkService.getAlbumList()
            cacheManager.addAlbumList(albumList)

            return albumList
        }
    }

    suspend fun createAlbum(album: Map<String, String>): AlbumList {
        try {
            val albumCreated = networkService.createAlbum(album)
            Log.d(TAG, "Album creado con éxito")
            return albumCreated
        } catch (e: Exception) {
            Log.e(TAG, "Error creando album", e)
            throw e
        }
    }    
}

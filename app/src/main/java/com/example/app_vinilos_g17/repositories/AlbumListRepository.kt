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

    suspend fun getAlbumList(): List<AlbumList> {
        val cachedAlbumList = cacheManager.getAlbumList()
        if (cachedAlbumList != null) {
            Log.d(TAG, "Se retorna información desde caché")
            return cachedAlbumList
        } else {
            Log.d(TAG, "Se retorna información desde el API")
            val albumList = networkService.getAlbumList()
            cacheManager.addAlbumList(albumList)

            return albumList
        }
    }
}

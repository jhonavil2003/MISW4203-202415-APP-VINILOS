package com.example.app_vinilos_g17.network

import android.content.Context
import android.util.Log
import android.util.LruCache
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.models.Collector

class CacheManager(context: Context) {

    companion object {
        private var instance: CacheManager? = null

        fun getInstance(context: Context): CacheManager {
            return instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
        }
    }

    //LruCache para detalle de album
    private var albumCache: LruCache<Int, Album> = LruCache(10)

    fun addAlbumDetails(albumId: Int, album: Album) {
        albumCache.put(albumId, album)
    }

    fun getAlbumDetails(albumId: Int): Album? {
        return albumCache.get(albumId)
    }

    //lruCache para listar coleccionistas
    private var collectorsCache: LruCache<String, List<Collector>> = LruCache(10)
    fun addCollectors(collectors: List<Collector>) {
        collectorsCache.put("collectors", collectors)
    }
    fun getCollectors(): List<Collector> {
        val cachedCollectors = collectorsCache.get("collectors")
        if (cachedCollectors != null && cachedCollectors.isNotEmpty()) {
            return cachedCollectors
        }
        return emptyList()
    }
}
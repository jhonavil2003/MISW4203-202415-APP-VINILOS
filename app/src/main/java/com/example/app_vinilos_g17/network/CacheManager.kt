package com.example.app_vinilos_g17.network

import android.content.Context
import android.util.LruCache
import com.example.app_vinilos_g17.models.Album

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
    private var albumCache: LruCache<Int, Album> = LruCache(10)

    // Función para agregar detalles de un álbum al caché
    fun addAlbumDetails(albumId: Int, album: Album) {
        albumCache.put(albumId, album)
    }

    // Función para obtener los detalles de un álbum desde el caché
    fun getAlbumDetails(albumId: Int): Album? {
        return albumCache.get(albumId)
    }
}
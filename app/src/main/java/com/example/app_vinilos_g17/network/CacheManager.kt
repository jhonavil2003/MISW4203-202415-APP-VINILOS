package com.example.app_vinilos_g17.network

import android.content.Context
import android.util.LruCache
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.models.AlbumList
import com.example.app_vinilos_g17.models.Collector

class CacheManager(context: Context) {

    companion object {
        const val DEFATUL_ALBUM_LIST_ID : Int= 0

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

    // Se crea solo un registro de caché de listado de albumes.
    private var albumListCache: LruCache<Int, List<AlbumList>> = LruCache(1)

    fun getAlbumList():  List<AlbumList>? {
        return albumListCache.get(DEFATUL_ALBUM_LIST_ID)
    }

    fun addAlbumList(albumList: List<AlbumList>) {
        albumListCache.put(DEFATUL_ALBUM_LIST_ID, albumList)
    }

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
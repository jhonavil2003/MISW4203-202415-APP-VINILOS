package com.example.app_vinilos_g17.network

import android.content.Context
import android.util.LruCache
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.models.AlbumList
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.models.Artist

class CacheManager(context: Context) {

    companion object {
        const val DEFAULT_ALBUM_LIST_ID : Int= 0

        const val DEFAULT_ARTIST_LIST_ID : Int= 0

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
        return albumListCache.get(DEFAULT_ALBUM_LIST_ID)
    }

    fun addAlbumList(albumList: List<AlbumList>) {
        albumListCache.put(DEFAULT_ALBUM_LIST_ID, albumList)
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

    private var artistCache: LruCache<Int, Artist> = LruCache(10)

    // Se crea solo un registro de caché de listado de artistas
    private var artistListCache: LruCache<Int, List<Artist>> = LruCache(1)
    fun getArtists():  List<Artist>? {
        return artistListCache.get(DEFAULT_ARTIST_LIST_ID)
    }
    fun addArtists(artistList: List<Artist>) {
        artistListCache.put(DEFAULT_ARTIST_LIST_ID, artistList)
    }

    fun addArtistDetail(artistId: Int, artist: Artist) {
        artistCache.put(artistId, artist)
    }

    fun getArtistDetail(artistId: Int): Artist? {
        return artistCache.get(artistId)
    }

    private var collectorDetailCache: LruCache<Int, Collector> = LruCache(10)

    fun addCollectorDetail(collectorId: Int, collectorDetail: Collector) {
        collectorDetailCache.put(collectorId, collectorDetail)
    }

    fun getCollectorDetail(collectorId: Int): Collector? {
        return collectorDetailCache.get(collectorId)
    }

}
package com.example.app_vinilos_g17.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.models.AlbumList
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.models.Comment
import com.example.app_vinilos_g17.models.Performer
import com.example.app_vinilos_g17.models.Track
import com.example.app_vinilos_g17.models.Artist
import com.example.app_vinilos_g17.models.SimpleAlbum
import com.example.app_vinilos_g17.models.PerformerPrize
import com.example.app_vinilos_g17.models.CollectorAlbum
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter(context: Context) {

    companion object {
        private const val TAG = "NetworkServiceAdapter"
        const val BASE_URL = "https://blackvynils-827885dbcaa3.herokuapp.com/"
        private var instance: NetworkServiceAdapter? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun getAlbumList() = suspendCoroutine<List<AlbumList>> { cont ->
        requestQueue.add(getRequest("albums",
            { response ->
                try {
                    val resp = JSONArray(response)
                    val list = mutableListOf<AlbumList>()
                    var item: JSONObject?  // Variable reutilizable para cada iteración

                    for (i in 0 until resp.length()) {
                        item = resp.getJSONObject(i) // Se reutiliza el espacio de memoria

                        // Obtener solo los nombres de los intérpretes
                        val performersArray = item.getJSONArray("performers")
                        val performerNames = mutableListOf<String>()
                        for (j in 0 until performersArray.length()) {
                            performerNames.add(
                                performersArray.getJSONObject(j).getString("name")
                            )
                        }

                        // Crear un objeto AlbumList con los campos necesarios
                        list.add(
                            AlbumList(
                                id = item.getInt("id"),
                                name = item.getString("name"),
                                cover = item.getString("cover"),
                                releaseDate = item.getString("releaseDate"),
                                performers = performerNames // Lista con los nombres de los intérpretes
                            )
                        )
                    }
                    // Reanudar la ejecución con el objeto album
                    cont.resume(list)
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            },
            { error ->
                cont.resumeWithException(error)
            }
        ))
    }

    // Optimización del método para obtener detalles de un álbum
    suspend fun getAlbumDetail(albumId: Int): Album = suspendCoroutine { cont ->
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                try {
                    val resp = JSONObject(response)

                    // Obtener la lista de performers
                    val performersArray = resp.getJSONArray("performers")
                    val performersList = mutableListOf<Performer>()
                    var performerObject: JSONObject?  // Variable reutilizable para cada iteración

                    for (j in 0 until performersArray.length()) {
                        performerObject =
                            performersArray.getJSONObject(j) // Se reutiliza el espacio de memoria

                        val birthDate = if (performerObject.has("birthDate")) {
                            performerObject.getString("birthDate")
                        } else {
                            "Fecha no disponible"
                        }

                        val performer = Performer(
                            id = performerObject.getInt("id"),
                            name = performerObject.getString("name"),
                            image = performerObject.getString("image"),
                            description = performerObject.getString("description"),
                            birthDate = birthDate
                        )

                        performersList.add(performer)
                    }

                    // Procesar tracks
                    val tracksArray = resp.getJSONArray("tracks")
                    val tracksList = mutableListOf<Track>()
                    var trackObject: JSONObject?  // Variable reutilizable para cada iteración

                    for (k in 0 until tracksArray.length()) {
                        trackObject =
                            tracksArray.getJSONObject(k) // Reutilizamos el espacio de memoria

                        val track = Track(
                            id = trackObject.getInt("id"),
                            name = trackObject.getString("name"),
                            duration = trackObject.getString("duration")
                        )
                        tracksList.add(track)
                    }

                    // Procesar comments
                    val commentsArray = resp.getJSONArray("comments")
                    val commentsList = mutableListOf<Comment>()
                    var commentObject: JSONObject?  // Variable reutilizable para cada iteración

                    for (l in 0 until commentsArray.length()) {
                        commentObject =
                            commentsArray.getJSONObject(l) // Reutilizamos el espacio de memoria

                        val comment = Comment(
                            id = commentObject.getInt("id"),
                            description = commentObject.getString("description"),
                            rating = commentObject.getString("rating")
                        )

                        commentsList.add(comment)
                    }

                    // Crear el objeto album con toda la información
                    val album = Album(
                        id = resp.getInt("id"),
                        name = resp.getString("name"),
                        cover = resp.getString("cover"),
                        recordLabel = resp.getString("recordLabel"),
                        releaseDate = resp.getString("releaseDate"),
                        genre = resp.getString("genre"),
                        description = resp.getString("description"),
                        performers = performersList,
                        tracks = tracksList,
                        comments = commentsList
                    )

                    // Reanudar la ejecución con el objeto album
                    cont.resume(album)
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            },
            { error ->
                cont.resumeWithException(error)
            }
        ))
    }

    suspend fun getCollectors() = suspendCoroutine<List<Collector>> { cont ->
        requestQueue.add(
            getRequest("collectors",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Collector>()
                    var item: JSONObject?  // Variable reutilizable para cada iteración

                    for (i in 0 until resp.length()) {
                        item = resp.getJSONObject(i) // Reutilizamos el espacio de memoria

                        val collector = Collector(
                            collectorId = item.getInt("id"),
                            name = item.getString("name"),
                            telephone = item.getString("telephone"),
                            email = item.getString("email")
                        )
                        list.add(collector)
                    }

                    cont.resume(list)
                },
                { error ->
                    cont.resumeWithException(error)
                })
        )
    }

    suspend fun getArtists() = suspendCoroutine<List<Artist>> { cont ->
        requestQueue.add(getRequest("musicians",
            { response ->
                try {
                    val artistArray = JSONArray(response)
                    val artistList = mutableListOf<Artist>()
                    var artistObject: JSONObject?  // Variable reutilizable para cada iteración

                    for (i in 0 until artistArray.length()) {
                        artistObject = artistArray.getJSONObject(i)

                        val albumArray = artistObject.getJSONArray("albums")
                        val albumList = mutableListOf<SimpleAlbum>()
                        var albumObject: JSONObject?  // Variable reutilizable para cada iteración

                        for (j in 0 until albumArray.length()) {
                            albumObject = albumArray.getJSONObject(j)

                            val album = SimpleAlbum(
                                albumObject.getInt("id"),
                                albumObject.getString("name"),
                                albumObject.getString("cover"),
                                albumObject.getString("description"),
                                albumObject.getString("genre"),
                                albumObject.getString("recordLabel")
                            )
                            albumList.add(album)
                        }

                        val performerPrizeArray = artistObject.getJSONArray("performerPrizes")
                        val performerPrizeList = mutableListOf<PerformerPrize>()
                        var performerPrizeObject: JSONObject?  // Variable reutilizable para cada iteración

                        for (k in 0 until performerPrizeArray.length()) {
                            performerPrizeObject = performerPrizeArray.getJSONObject(k)

                            val performerPrize = PerformerPrize(
                                performerPrizeObject.getInt("id"),
                                performerPrizeObject.getString("premiationDate"),
                            )
                            performerPrizeList.add(performerPrize)
                        }

                        artistList.add(i, Artist(
                            id = artistObject.getInt("id"),
                            name = artistObject.getString("name"),
                            image = artistObject.getString("image"),
                            description = artistObject.getString("description"),
                            birthDate = artistObject.getString("birthDate"),
                            albums = albumList,
                            performerPrizes = performerPrizeList)
                        )
                    }
                    cont.resume(artistList)
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            },
            { error ->
                cont.resumeWithException(error)
            }))
    }


    suspend fun getArtistDetail(artistId: Int): Artist = suspendCoroutine { cont ->
        requestQueue.add(getRequest("musicians/$artistId",
            { response ->
                try {
                    val artistObject = JSONObject(response)

                    val albumArray = artistObject.getJSONArray("albums")
                    val albumList = mutableListOf<SimpleAlbum>()
                    var albumObject: JSONObject?  // Variable reutilizable para cada iteración

                    for (j in 0 until albumArray.length()) {
                        albumObject = albumArray.getJSONObject(j)

                        val album = SimpleAlbum(
                            albumObject.getInt("id"),
                            albumObject.getString("name"),
                            albumObject.getString("cover"),
                            albumObject.getString("description"),
                            albumObject.getString("genre"),
                            albumObject.getString("recordLabel")
                        )
                        albumList.add(album)
                    }

                    val performerPrizeArray = artistObject.getJSONArray("performerPrizes")
                    val performerPrizeList = mutableListOf<PerformerPrize>()
                    var performerPrizeObject: JSONObject?  // Variable reutilizable para cada iteración

                    for (k in 0 until performerPrizeArray.length()) {
                        performerPrizeObject = performerPrizeArray.getJSONObject(k)

                        val performerPrize = PerformerPrize(
                            performerPrizeObject.getInt("id"),
                            performerPrizeObject.getString("premiationDate"),
                        )
                        performerPrizeList.add(performerPrize)
                    }

                     val artist = Artist(
                        id = artistObject.getInt("id"),
                        name = artistObject.getString("name"),
                        image = artistObject.getString("image"),
                        description = artistObject.getString("description"),
                        birthDate = artistObject.getString("birthDate"),
                        albums = albumList,
                        performerPrizes = performerPrizeList
                     )

                    // Reanudar la ejecución con el objeto album
                    cont.resume(artist)
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            },
            { error ->
                cont.resumeWithException(error)
            }
        ))
    }

    suspend fun getCollectorDetail(collectorId: Int) = suspendCoroutine<Collector> { cont ->
        requestQueue.add(
            getRequest("collectors/$collectorId",
                { response ->
                    try {
                        val item = JSONObject(response)

                        val comments = mutableListOf<Comment>()
                        val commentsArray = item.getJSONArray("comments")
                        for (i in 0 until commentsArray.length()) {
                            val commentObj = commentsArray.getJSONObject(i)
                            val comment = Comment(
                                id = commentObj.getInt("id"),
                                description = commentObj.getString("description"),
                                rating = commentObj.getInt("rating").toString()
                            )
                            comments.add(comment)
                        }

                        val favoritePerformers = mutableListOf<Performer>()
                        val performersArray = item.getJSONArray("favoritePerformers")
                        for (i in 0 until performersArray.length()) {
                            val performerObj = performersArray.getJSONObject(i)

                            // Asignar creación de fecha y nacimiento de manera segura
                            val creationDate = performerObj.optString("creationDate", "No Date Available")
                            val birthDate = performerObj.optString("birthDate", "No Date Available")

                            val performer = Performer(
                                id = performerObj.getInt("id"),
                                name = performerObj.getString("name"),
                                image = performerObj.getString("image"),
                                description = performerObj.getString("description"),
                                birthDate = birthDate,
                                creationDate = creationDate
                            )
                            favoritePerformers.add(performer)
                        }

                        val collectorAlbums = mutableListOf<CollectorAlbum>()
                        val albumsArray = item.getJSONArray("collectorAlbums")
                        for (i in 0 until albumsArray.length()) {
                            val albumObj = albumsArray.getJSONObject(i)
                            val album = CollectorAlbum(
                                id = albumObj.getInt("id"),
                                price = albumObj.getInt("price"),
                                status = albumObj.getString("status")
                            )
                            collectorAlbums.add(album)
                        }

                        val collector = Collector(
                            collectorId = item.getInt("id"),
                            name = item.getString("name"),
                            telephone = item.getString("telephone"),
                            email = item.getString("email"),
                            comments = comments,
                            favoritePerformers = favoritePerformers,
                            collectorAlbums = collectorAlbums
                        )

                        cont.resume(collector)
                    } catch (e: Exception) {
                        cont.resumeWithException(e)
                    }
                },
                { error ->
                    cont.resumeWithException(error)
                }
            )
        )
    }



    suspend fun createAlbum(album: Map<String, String>): AlbumList = suspendCoroutine { cont ->
        val requestBody = JSONObject(album)
        Log.d(TAG, "Intentando crear álbum: $requestBody")

        requestQueue.add(postRequest(
            path = "albums",
            requestBody = requestBody,
            onSuccess = { response ->
                try {
                    val createdAlbum = AlbumList(
                        id = response.getInt("id"),
                        name = response.getString("name"),
                        cover = response.getString("cover"),
                        releaseDate = response.getString("releaseDate"),
                        performers = emptyList()
                    )

                    // Retornar el objeto AlbumList
                    cont.resume(createdAlbum)
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            },
            onError = { error ->
                // Manejar errores de red o parsing
                cont.resumeWithException(error)
            }
        ))
    }

    suspend fun setTrack(albumId: Int, album: Map<String, String>): String = suspendCoroutine { cont ->
        val requestBody = JSONObject(album)
        Log.d(TAG, "Intentando crear track: $requestBody")

        requestQueue.add(postRequest(
            path = "albums/$albumId/tracks",
            requestBody = requestBody,
            onSuccess = { response ->
                try {
                    val createdAlbum = AlbumList(
                        id = response.getInt("id"),
                        name = response.getString("name"),
                        cover = response.getString("cover"),
                        releaseDate = response.getString("releaseDate"),
                        performers = emptyList()
                    )

                    // Retornar el objeto AlbumList
                    cont.resume(response.toString())
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            },
            onError = { error ->
                // Manejar errores de red o parsing
                cont.resumeWithException(error)
            }
        ))
    }

    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }

    private fun postRequest(
        path: String,
        requestBody: JSONObject,
        onSuccess: (JSONObject) -> Unit,
        onError: (Exception) -> Unit
    ): JsonObjectRequest {
        return object : JsonObjectRequest(
            Method.POST,
            BASE_URL + path,
            requestBody,
            { response ->
                try {
                    onSuccess(response)
                } catch (e: Exception) {
                    onError(e)
                }
            },
            { error ->
                onError(Exception("Error en la solicitud: ${error.networkResponse?.statusCode} - ${error.message}"))
            }
        ) {
            override fun getPriority(): Priority {
                return Priority.HIGH
            }
        }
    }
}

package com.example.app_vinilos_g17.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.models.AlbumList
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.models.Comment
import com.example.app_vinilos_g17.models.Performer
import com.example.app_vinilos_g17.models.Track
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter(context: Context) {

    companion object {
        const val BASE_URL = "https://blackvynils-827885dbcaa3.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null

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

    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }
}

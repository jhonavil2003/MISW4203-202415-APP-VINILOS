package com.example.app_vinilos_g17.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.models.Comment
import com.example.app_vinilos_g17.models.Performer
import com.example.app_vinilos_g17.models.Track
import org.json.JSONArray
import org.json.JSONObject

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

    fun getAlbums(onComplete: (resp: List<Album>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("albums",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    val performersArray = item.getJSONArray("performers")
                    val performersList = mutableListOf<Performer>()

                    for (j in 0 until performersArray.length()) {
                        val performerObject = performersArray.getJSONObject(j)

                        // Verificar si birthDate está presente
                        val birthDate = if (performerObject.has("birthDate")) {
                            performerObject.getString("birthDate")
                        } else {
                            "Fecha no disponible"
                        }

                        val performer = Performer(
                            performerObject.getInt("id"),
                            performerObject.getString("name"),
                            performerObject.getString("image"),
                            performerObject.getString("description"),
                            birthDate
                        )
                        performersList.add(performer)
                    }

                    // Procesar tracks
                    val tracksArray = item.getJSONArray("tracks")
                    val tracksList = mutableListOf<Track>()
                    for (k in 0 until tracksArray.length()) {
                        val trackObject = tracksArray.getJSONObject(k)
                        val track = Track(
                            id = trackObject.getInt("id"),
                            name = trackObject.getString("name"),
                            duration = trackObject.getString("duration")
                        )
                        tracksList.add(track)
                    }

                    // Procesar comments
                    val commentsArray = item.getJSONArray("comments")
                    val commentsList = mutableListOf<Comment>()
                    for (l in 0 until commentsArray.length()) {
                        val commentObject = commentsArray.getJSONObject(l)
                        val comment = Comment(
                            id = commentObject.getInt("id"),
                            description = commentObject.getString("description"),
                            rating = commentObject.getString("rating")
                        )
                        commentsList.add(comment)
                    }

                    list.add(Album(
                        id = item.getInt("id"),
                        name = item.getString("name"),
                        cover = item.getString("cover"),
                        recordLabel = item.getString("recordLabel"),
                        releaseDate = item.getString("releaseDate"),
                        genre = item.getString("genre"),
                        description = item.getString("description"),
                        performers = performersList,
                        tracks = tracksList,
                        comments = commentsList
                    ))
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }


    fun getAlbumDetail(albumId: Int, onComplete: (album: Album) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val resp = JSONObject(response)

                // Obtener la lista de performers
                val performersArray = resp.getJSONArray("performers")
                val performersList = mutableListOf<Performer>()

                for (j in 0 until performersArray.length()) {
                    val performerObject = performersArray.getJSONObject(j)

                    // Verificar si birthDate está presente
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
                for (k in 0 until tracksArray.length()) {
                    val trackObject = tracksArray.getJSONObject(k)
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
                for (l in 0 until commentsArray.length()) {
                    val commentObject = commentsArray.getJSONObject(l)
                    val comment = Comment(
                        id = commentObject.getInt("id"),
                        description = commentObject.getString("description"),
                        rating = commentObject.getString("rating")
                    )
                    commentsList.add(comment)
                }

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
                onComplete(album)
            },
            {
                onError(it)
            }))
    }


    private fun getRequest(path: String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }
}

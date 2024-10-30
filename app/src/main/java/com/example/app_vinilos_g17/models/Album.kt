package com.example.app_vinilos_g17.models

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: String,
    val performers: List<Performer>
)

package com.example.app_vinilos_g17.models

data class AlbumList(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val performers: List<String>
)
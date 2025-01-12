package com.example.app_vinilos_g17.models

data class Collector(
    val collectorId: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<Comment> = emptyList(),
    val favoritePerformers: List<Performer> = emptyList(),
    var collectorAlbums: List<CollectorAlbum> = emptyList()
)

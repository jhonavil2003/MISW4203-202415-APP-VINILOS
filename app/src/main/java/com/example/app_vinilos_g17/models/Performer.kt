package com.example.app_vinilos_g17.models

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String? = "No Date Available",
    val creationDate: String? = "No Date Available"

)

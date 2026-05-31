package com.dierlisson.rickmortyhub.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String,
    val type: String = "",
    val gender: String = "",
    val originName: String = "",
    val locationName: String = "",
    var isFavorite: Boolean = false 
)

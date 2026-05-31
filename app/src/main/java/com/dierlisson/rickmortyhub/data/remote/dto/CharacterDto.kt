package com.dierlisson.rickmortyhub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CharacterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: OriginDto,
    @SerializedName("location") val location: LocationDto,
    @SerializedName("image") val image: String,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
)

data class OriginDto(
    @SerializedName("name") val name: String, 
    @SerializedName("url") val url: String
)

data class LocationDto(
    @SerializedName("name") val name: String, 
    @SerializedName("url") val url: String
)

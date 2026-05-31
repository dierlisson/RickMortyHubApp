package com.dierlisson.rickmortyhub.data.remote.api

import com.dierlisson.rickmortyhub.data.remote.dto.CharacterDto
import com.dierlisson.rickmortyhub.data.remote.dto.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("gender") gender: String? = null
    ): Response<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Response<CharacterDto>
}

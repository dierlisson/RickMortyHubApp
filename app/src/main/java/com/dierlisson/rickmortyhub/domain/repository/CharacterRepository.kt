package com.dierlisson.rickmortyhub.domain.repository

import com.dierlisson.rickmortyhub.domain.model.Character
import com.dierlisson.rickmortyhub.utils.Resource

interface CharacterRepository {
    suspend fun getCharacters(page: Int, name: String? = null, status: String? = null, gender: String? = null): Resource<List<Character>>
    suspend fun getCharacterById(id: Int): Resource<Character>
    suspend fun getFavorites(): List<Character>
    suspend fun toggleFavorite(character: Character)
}

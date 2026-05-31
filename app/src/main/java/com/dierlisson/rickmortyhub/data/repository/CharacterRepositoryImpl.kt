package com.dierlisson.rickmortyhub.data.repository

import com.dierlisson.rickmortyhub.data.local.dao.CharacterDao
import com.dierlisson.rickmortyhub.data.mapper.toDomainModel
import com.dierlisson.rickmortyhub.data.mapper.toEntity
import com.dierlisson.rickmortyhub.data.remote.api.RickMortyApi
import com.dierlisson.rickmortyhub.domain.model.Character
import com.dierlisson.rickmortyhub.domain.repository.CharacterRepository
import com.dierlisson.rickmortyhub.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepositoryImpl(
    private val api: RickMortyApi,
    private val dao: CharacterDao
) : CharacterRepository {

    override suspend fun getCharacters(page: Int, name: String?, status: String?, gender: String?): Resource<List<Character>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCharacters(page, name, status, gender)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val characters = body.results.map { dto ->
                            val isFav = dao.isFavorite(dto.id)
                            dto.toDomainModel().copy(isFavorite = isFav)
                        }
                        Resource.Success(characters)
                    } ?: Resource.Error("Resposta vazia da API")
                } else {
                    if (response.code() == 404) {
                        Resource.Success(emptyList())
                    } else {
                        Resource.Error("Erro ao buscar dados: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Resource.Error("Erro de conexão. Verifique sua internet.")
            }
        }
    }

    override suspend fun getCharacterById(id: Int): Resource<Character> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCharacterById(id)
                if (response.isSuccessful) {
                    response.body()?.let { dto ->
                        val isFav = dao.isFavorite(dto.id)
                        Resource.Success(dto.toDomainModel().copy(isFavorite = isFav))
                    } ?: Resource.Error("Personagem não encontrado")
                } else {
                    Resource.Error("Erro ao buscar detalhes: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Erro de conexão. Verifique sua internet.")
            }
        }
    }

    override suspend fun getFavorites(): List<Character> {
        return withContext(Dispatchers.IO) {
            dao.getAllFavorites().map { it.toDomainModel() }
        }
    }

    override suspend fun toggleFavorite(character: Character) {
        withContext(Dispatchers.IO) {
            val isFavorite = dao.isFavorite(character.id)
            if (isFavorite) {
                dao.deleteFavoriteById(character.id)
            } else {
                dao.insertFavorite(character.toEntity())
            }
        }
    }
}

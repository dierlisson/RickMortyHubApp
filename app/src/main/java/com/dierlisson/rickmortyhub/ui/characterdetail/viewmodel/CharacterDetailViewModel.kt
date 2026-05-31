package com.dierlisson.rickmortyhub.ui.characterdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dierlisson.rickmortyhub.domain.model.Character
import com.dierlisson.rickmortyhub.domain.repository.CharacterRepository
import com.dierlisson.rickmortyhub.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailState(
    val character: Character? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class CharacterDetailViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailState())
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    fun loadCharacter(id: Int) {
        _state.value = CharacterDetailState(isLoading = true)
        
        viewModelScope.launch {
            val result = repository.getCharacterById(id)
            when (result) {
                is Resource.Success -> {
                    _state.value = CharacterDetailState(character = result.data, isLoading = false)
                }
                is Resource.Error -> {
                    _state.value = CharacterDetailState(error = result.message, isLoading = false)
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun toggleFavorite() {
        val character = _state.value.character ?: return
        viewModelScope.launch {
            repository.toggleFavorite(character)
            _state.value = _state.value.copy(
                character = character.copy(isFavorite = !character.isFavorite)
            )
        }
    }
}

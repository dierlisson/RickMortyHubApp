package com.dierlisson.rickmortyhub.ui.characterlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dierlisson.rickmortyhub.domain.model.Character
import com.dierlisson.rickmortyhub.domain.repository.CharacterRepository
import com.dierlisson.rickmortyhub.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterListState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val isEndOfPaginationReached: Boolean = false
)

class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterListState())
    val state: StateFlow<CharacterListState> = _state.asStateFlow()

    private var currentPage = 1
    private var currentQuery: String? = null
    private var currentStatus: String? = null
    private var currentGender: String? = null
    
    private var isRequestInProgress = false

    init {
        loadCharacters()
    }

    fun searchCharacters(query: String) {
        if (currentQuery == query) return
        currentQuery = query.ifBlank { null }
        currentPage = 1
        
        _state.value = CharacterListState(isLoading = true) 
        loadCharacters()
    }

    fun filterBy(status: String?, gender: String?) {
        if (currentStatus == status && currentGender == gender) return
        currentStatus = status
        currentGender = gender
        currentPage = 1
        
        _state.value = CharacterListState(isLoading = true)
        loadCharacters()
    }

    fun loadNextPage() {
        if (isRequestInProgress || _state.value.isEndOfPaginationReached || _state.value.isLoading) return
        
        _state.value = _state.value.copy(isLoadingMore = true)
        loadCharacters(isLoadMore = true)
    }

    private fun loadCharacters(isLoadMore: Boolean = false) {
        isRequestInProgress = true
        
        val pageToLoad = if (isLoadMore) currentPage + 1 else 1

        viewModelScope.launch {
            val result = repository.getCharacters(pageToLoad, currentQuery, currentStatus, currentGender)
            
            when (result) {
                is Resource.Success -> {
                    currentPage = pageToLoad
                    val newCharacters = result.data ?: emptyList()
                    val currentList = if (isLoadMore) _state.value.characters else emptyList()
                    
                    _state.value = _state.value.copy(
                        characters = currentList + newCharacters,
                        isLoading = false,
                        isLoadingMore = false,
                        error = null,
                        isEndOfPaginationReached = newCharacters.isEmpty()
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> Unit
            }
            isRequestInProgress = false
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            repository.toggleFavorite(character)
            
            val updatedList = _state.value.characters.map {
                if (it.id == character.id) it.copy(isFavorite = !it.isFavorite) else it
            }
            _state.value = _state.value.copy(characters = updatedList)
        }
    }
}

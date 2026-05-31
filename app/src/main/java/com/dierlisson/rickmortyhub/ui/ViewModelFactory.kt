package com.dierlisson.rickmortyhub.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dierlisson.rickmortyhub.domain.repository.CharacterRepository
import com.dierlisson.rickmortyhub.ui.characterdetail.viewmodel.CharacterDetailViewModel
import com.dierlisson.rickmortyhub.ui.characterlist.viewmodel.CharacterListViewModel

class ViewModelFactory(
    private val repository: CharacterRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterListViewModel::class.java)) {
            return CharacterListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}

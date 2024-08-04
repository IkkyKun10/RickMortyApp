package com.riezki.rickmortyapp.presenter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riezki.network.model.domain.CharacterPage
import com.riezki.rickmortyapp.presenter.actions.HomeScreenViewState
import com.riezki.rickmortyapp.repositories.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author riezky maisyar
 */

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState.Loading)
    val stateFlow = _viewState.asStateFlow()

    private val fetchCharacterPages = mutableListOf<CharacterPage>()

    fun fetchInitialPage() = viewModelScope.launch {
        if (fetchCharacterPages.isNotEmpty()) return@launch
        val initialPage = repository.fetchCharacterPage(1)
        initialPage
            .onSuccess { characterPage ->
                fetchCharacterPages.clear()
                fetchCharacterPages.add(characterPage)

                _viewState.update {
                    return@update HomeScreenViewState.GridDisplay(characters = characterPage.characters)
                }
            }
            .onFailure { }
    }

    fun fetchNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchCharacterPages.size + 1
        val result = repository.fetchCharacterPage(page = nextPageIndex)

        result
            .onSuccess { characterPage ->
                fetchCharacterPages.add(characterPage)
                _viewState.update { current ->
                    val currentCharacter = (current as HomeScreenViewState.GridDisplay).characters ?: emptyList()
                    return@update HomeScreenViewState.GridDisplay(
                        characters = currentCharacter + (characterPage.characters ?: emptyList())
                    )
                }
            }
            .onFailure {  }

    }
}
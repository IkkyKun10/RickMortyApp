package com.riezki.rickmortyapp.presenter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riezki.rickmortyapp.component.common.DataPoint
import com.riezki.rickmortyapp.presenter.actions.CharacterDetailsViewState
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
class CharacterDetailViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _internalStorageFlow = MutableStateFlow<CharacterDetailsViewState>(CharacterDetailsViewState.Loading)
    val stateFlow = _internalStorageFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        _internalStorageFlow.update { return@update CharacterDetailsViewState.Loading }
        repository.fetchCharacter(characterId)
            .onSuccess { character ->
                val dataPoints = buildList<DataPoint> {
                    add(DataPoint("Last known location", character.location.name.toString()))
                    add(DataPoint("Species", character.species.toString()))
                    add(DataPoint("Gender", character.gender.gender))
                    character.type.takeIf { it?.isNotEmpty() == true }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Origin", character.origin.name.toString()))
                    add(DataPoint("Episode count", character.episodeIds?.size.toString()))
                }

                _internalStorageFlow.update {
                    return@update CharacterDetailsViewState.Success(
                        character = character,
                        characterDataPoints = dataPoints
                    )
                }
            }
            .onFailure { e ->
                _internalStorageFlow.update {
                    return@update CharacterDetailsViewState.Error(
                        message = e.message ?: "Unknown error occurred"
                    )
                }
            }
    }
}
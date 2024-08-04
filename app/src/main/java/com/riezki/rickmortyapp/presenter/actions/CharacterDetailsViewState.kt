package com.riezki.rickmortyapp.presenter.actions

import com.riezki.network.model.domain.Character
import com.riezki.rickmortyapp.component.common.DataPoint

/**
 * @author riezky maisyar
 */

sealed interface CharacterDetailsViewState {
    data object Loading : CharacterDetailsViewState
    data class Success(
        val character: Character,
        val characterDataPoints: List<DataPoint>
    ) : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
}
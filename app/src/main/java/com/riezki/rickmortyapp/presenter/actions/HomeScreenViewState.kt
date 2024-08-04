package com.riezki.rickmortyapp.presenter.actions

import com.riezki.network.model.domain.Character

/**
 * @author riezky maisyar
 */

sealed interface HomeScreenViewState {
    data object Loading : HomeScreenViewState
    data class GridDisplay(val characters: List<Character>? = emptyList()) : HomeScreenViewState
}
package com.riezki.network.model.domain

import androidx.compose.ui.graphics.Color

/**
 * @author riezky maisyar
 */

sealed class CharacterGender(val gender: String) {
    data object Male : CharacterGender("Male")
    data object Female : CharacterGender("Female")
    data object Genderless : CharacterGender("No gender")
    data object Unknown : CharacterGender("Not specified")
}
package com.riezki.network.model.domain

import androidx.compose.ui.graphics.Color

/**
 * @author riezky maisyar
 */

sealed class CharacterGender(val gender: String) {
    object Male : CharacterGender("Male")
    object Female : CharacterGender("Female")
    object Genderless : CharacterGender("No gender")
    object Unknown : CharacterGender("Not specified")
}
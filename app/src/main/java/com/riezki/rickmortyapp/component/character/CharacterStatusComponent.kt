package com.riezki.rickmortyapp.component.character

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riezki.network.model.domain.CharacterStatus
import com.riezki.rickmortyapp.ui.theme.RickMortyAppTheme
import com.riezki.rickmortyapp.ui.theme.RickTextPrimary

/**
 * @author riezky maisyar
 */

@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = characterStatus.color,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = "Status: ${characterStatus.displayName}",
            fontSize = 20.sp,
            color = RickTextPrimary,
        )
    }
}

@Preview
@Composable
fun PrevCharacterStatComp() {
    RickMortyAppTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Alive)
    }
}


@Preview
@Composable
fun CharacterStatusComponentPreviewDead() {
    RickMortyAppTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Dead)
    }
}

@Preview
@Composable
fun CharacterStatusComponentPreviewUnknown() {
    RickMortyAppTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Unknown)
    }
}
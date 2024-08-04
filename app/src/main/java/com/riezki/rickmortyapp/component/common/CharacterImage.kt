package com.riezki.rickmortyapp.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

/**
 * @author riezky maisyar
 */

private val defaultModifier = Modifier
    .fillMaxWidth()
    .aspectRatio(1f)
    .clip(RoundedCornerShape(12.dp))

@Composable
fun CharacterImage(imageUrl: String, modifier: Modifier = defaultModifier) {
    SubcomposeAsyncImage(
        model = imageUrl, contentDescription = "Character Image",
        modifier = modifier,
        loading = {
            LoadingState()
        }
    )
}

@Preview
@Composable
fun CharacterImagePrev() {
    CharacterImage(
        imageUrl = "Image Url", modifier = defaultModifier.then(
            Modifier.background(
                brush = Brush.verticalGradient(listOf(Color.White, Color.Black))
            )
        )
    )
}
package com.riezki.rickmortyapp.presenter.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riezki.rickmortyapp.component.character.CharacterDetailsNamePlateComponent
import com.riezki.rickmortyapp.component.common.DataPointComponent
import com.riezki.rickmortyapp.component.common.LoadingState
import com.riezki.rickmortyapp.component.common.SimpleToolbar
import com.riezki.rickmortyapp.presenter.actions.CharacterDetailsViewState
import com.riezki.rickmortyapp.presenter.viewmodels.CharacterDetailViewModel
import com.riezki.rickmortyapp.ui.theme.RickAction

/**
 * @author riezky maisyar
 */

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailViewModel = hiltViewModel(),
    onEpisodeClicked: (Int) -> Unit,
    onBackClicked: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchCharacter(characterId)
    }

    val state by viewModel.stateFlow.collectAsState()

    Column {
        SimpleToolbar(title = "Character Details", onBackAction = onBackClicked)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            when (val viewState = state) {
                CharacterDetailsViewState.Loading -> item { LoadingState() }
                is CharacterDetailsViewState.Error -> {}
                is CharacterDetailsViewState.Success -> {
                    item {
                        CharacterDetailsNamePlateComponent(
                            name = viewState.character.name.toString(),
                            status = viewState.character.status
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(viewState.characterDataPoints) {
                        Spacer(modifier = Modifier.height(32.dp))
                        DataPointComponent(dataPoint = it)
                    }

                    item { Spacer(modifier = Modifier.height(32.dp)) }

                    item {
                        Text(
                            text = "View All Episodes",
                            color = RickAction,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(
                                    horizontal = 32.dp
                                )
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    color = RickAction
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onEpisodeClicked(characterId) }
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        )
                    }

                    item { Spacer(modifier = Modifier.height(64.dp)) }
                }
            }
        }
    }
}
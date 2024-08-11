package com.riezki.rickmortyapp.presenter.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riezki.network.KtorClient
import com.riezki.network.model.domain.Character
import com.riezki.network.model.domain.Episode
import com.riezki.rickmortyapp.component.common.CharacterImage
import com.riezki.rickmortyapp.component.common.CharacterNameComponent
import com.riezki.rickmortyapp.component.common.DataPoint
import com.riezki.rickmortyapp.component.common.DataPointComponent
import com.riezki.rickmortyapp.component.common.LoadingState
import com.riezki.rickmortyapp.component.common.SimpleToolbar
import com.riezki.rickmortyapp.component.episode.EpisodeRowComponent
import com.riezki.rickmortyapp.ui.theme.RickPrimary
import com.riezki.rickmortyapp.ui.theme.RickTextPrimary
import kotlinx.coroutines.launch

/**
 * @author riezky maisyar
 */

@Composable
fun SeasonHeader(
    seasonNumber: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = RickPrimary)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Season $seasonNumber",
            color = RickTextPrimary,
            fontSize = 32.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = RickTextPrimary
                )
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun CharacterEpisodeScreen(
    characterId: Int,
    ktorClient: KtorClient,
    onBackClicked: () -> Unit
) {
    var characterState by remember {
        mutableStateOf<Character?>(null)
    }
    var episodesState by remember {
        mutableStateOf<List<Episode>>(emptyList())
    }

    LaunchedEffect(key1 = Unit) {
        ktorClient.getCharacter(characterId)
            .onSuccess { character ->
                characterState = character

                launch {
                    character.episodeIds?.let {
                        ktorClient.getEpisodes(it)
                            .onSuccess { episodes ->
                                episodesState = episodes
                                println("episodesState: \n$episodesState")
                            }
                            .onFailure {

                            }
                    }
                }
            }
            .onFailure {

            }
    }

    characterState?.let { character ->
        MainScreen(character = character, episodes = episodesState, onBackClicked = onBackClicked)
    } ?: LoadingState()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    character: Character,
    episodes: List<Episode>,
    onBackClicked: () -> Unit
) {
    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }

    Column {
        SimpleToolbar(
            title = "Character Episode",
            onBackAction = onBackClicked
        )
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item { CharacterNameComponent(name = character.name.toString()) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                LazyRow {
                    episodeBySeasonMap.forEach { mapEntry ->
                        val title = "Season ${mapEntry.key}"
                        val desc = "${mapEntry.value.size} ep"
                        item { 
                            DataPointComponent(dataPoint = DataPoint(title, desc))
                            Spacer(modifier = Modifier.width(32.dp))
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { CharacterImage(imageUrl = character.imageUrl.toString()) }
            item { Spacer(modifier = Modifier.height(32.dp)) }

            episodeBySeasonMap.forEach { mapEntry ->
                stickyHeader { SeasonHeader(seasonNumber = mapEntry.key ?: 1) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                items(mapEntry.value) { episode ->
                    EpisodeRowComponent(episode = episode)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
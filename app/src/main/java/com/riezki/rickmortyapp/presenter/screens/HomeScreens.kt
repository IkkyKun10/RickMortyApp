package com.riezki.rickmortyapp.presenter.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riezki.rickmortyapp.component.character.CharacterGridItem
import com.riezki.rickmortyapp.component.common.LoadingState
import com.riezki.rickmortyapp.component.common.SimpleToolbar
import com.riezki.rickmortyapp.presenter.actions.HomeScreenViewState
import com.riezki.rickmortyapp.presenter.viewmodels.HomeScreenViewModel

/**
 * @author riezky maisyar
 */

@Composable
fun HomeScreens(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCharacterSelected: (Int) -> Unit,
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(key1 = viewModel, block = { viewModel.fetchInitialPage() })

    val scrollState = rememberLazyStaggeredGridState()
    val fetchNextPage: Boolean by remember {
        derivedStateOf {
            val currentCharacterCount =
                (viewState as? HomeScreenViewState.GridDisplay)?.characters?.size
                    ?: return@derivedStateOf false
            val lastDisplayedIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            return@derivedStateOf lastDisplayedIndex >= currentCharacterCount - 10
        }
    }

    LaunchedEffect(key1 = fetchNextPage, block = {
        if (fetchNextPage) viewModel.fetchNextPage()
    })

    when (val state = viewState) {
        HomeScreenViewState.Loading -> LoadingState()
        is HomeScreenViewState.GridDisplay -> {
            Column {
                SimpleToolbar(title = "All Characters")
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    state = scrollState,
                    contentPadding = PaddingValues(16.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(
                            state.characters ?: emptyList(),
                            key = { it.id!! }
                        ) { character ->
                            CharacterGridItem(
                                modifier = Modifier,
                                character = character
                            ) {
                                character.id?.let { onCharacterSelected(it) }
                            }
                        }
                    }
                )
            }
        }
    }

}
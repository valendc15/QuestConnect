package com.questconnect.games

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.questconnect.R
import com.questconnect.ui.theme.largeText
import com.questconnect.ui.theme.mediumSmallText
import com.questconnect.ui.theme.mediumText

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import com.questconnect.ui.theme.basicDimension
import com.questconnect.ui.theme.bigDimension
import com.questconnect.ui.theme.gridDimension
import com.questconnect.ui.theme.halfBasicDimension
import com.questconnect.ui.theme.mediumDimension
import com.questconnect.ui.theme.minutesValue
import com.questconnect.utils.SteamNotAvailable

@Composable
fun Games() {
    val viewModel = hiltViewModel<GamesViewModel>()
    val isSteamIdAvailable = viewModel.isSteamIdAvailable.collectAsState()
    
    if (!isSteamIdAvailable.value){
        SteamNotAvailable(stringId = R.string.not_sign_in_steam_games)
    }
    else {
        GamesLibrary(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesLibrary(viewModel: GamesViewModel) {
    val games = viewModel.games.collectAsState()
    val loadingGames = viewModel.loadingGames.collectAsState()
    val showRetry = viewModel.showRetry.collectAsState()
    val searchQuery = viewModel.searchQuery.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            SearchBar(
                colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.primary),
                query = searchQuery.value,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onSearch = {},
                active = false,
                onActiveChange = { /* Handle active change */ },
                placeholder = { Text(stringResource(id = R.string.search_games)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = mediumDimension),
                content = {}
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when {
                    loadingGames.value -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .size(bigDimension)
                                    .align(Alignment.Center),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.background
                            )
                        }
                    }

                    showRetry.value -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.retry),
                                fontSize = largeText,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = stringResource(id = R.string.retry_games),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Button(onClick = { viewModel.retryLoadingGames() }) {
                                Text(text = stringResource(id = R.string.retry))
                            }
                        }
                    }

                    else -> {
                        val filteredGames = games.value.filter {
                            it.name.contains(searchQuery.value, ignoreCase = true)
                        }

                        if (filteredGames.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.no_games_found),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = largeText
                                )
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = gridDimension),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredGames) { game: Game ->
                                    GameView(game = game, viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameView(game: Game, viewModel: GamesViewModel) {
    val favoriteGameIds by viewModel.favoriteGameIds.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }

    val isFav = favoriteGameIds.contains(game.appid.toLong())

    Card(
        modifier = Modifier
            .padding(halfBasicDimension)
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(basicDimension),
        elevation = CardDefaults.cardElevation(halfBasicDimension)
    ) {
        Box {
            val imageUrl = stringResource(id = R.string.steam_image_url, game.appid)
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = imageUrl)
                    .apply {
                        crossfade(true)
                        error(R.drawable.icons8_steam)
                    }
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = game.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Icon(
                imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = stringResource(id = R.string.favorite),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(basicDimension)
                    .clickable { viewModel.toggleFavoriteGame(game) },
                tint = MaterialTheme.colorScheme.tertiary
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            RoundedCornerShape(basicDimension)
                        )
                        .padding(basicDimension),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = game.name,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = mediumText
                        )
                        Spacer(modifier = Modifier.height(halfBasicDimension))
                        Text(
                            text = stringResource(id = R.string.total_playtime, (game.playtime_forever / minutesValue).toString()),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = mediumSmallText
                        )
                    }
                }
            }
        }
    }
}
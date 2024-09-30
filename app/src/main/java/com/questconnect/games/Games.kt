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
import com.questconnect.ui.theme.SteamBlue
import com.questconnect.ui.theme.SteamLightBlue
import com.questconnect.ui.theme.largeText
import com.questconnect.ui.theme.mediumSmallText
import com.questconnect.ui.theme.mediumText

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Games() {
    val viewModel = hiltViewModel<GamesViewModel>()
    val games = viewModel.games.collectAsState()
    val loadingGames = viewModel.loadingGames.collectAsState()
    val showRetry = viewModel.showRetry.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val searchQuery = viewModel.searchQuery.collectAsState()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearSnackbar()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SteamBlue)
                .padding(8.dp)
        ) {
            SearchBar(
                query = searchQuery.value,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onSearch = {},
                active = false,
                onActiveChange = { /* Handle active change */ },
                placeholder = { Text("Search for games") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                modifier = Modifier
                    .fillMaxWidth().padding(bottom = 18.dp),
                content = {}
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SteamBlue)
            ) {
                when {
                    loadingGames.value -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .size(64.dp)
                                    .align(Alignment.Center),
                                color = SteamLightBlue,
                                trackColor = SteamBlue
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
                                color = Color.White
                            )
                            Text(
                                text = stringResource(id = R.string.retry_games),
                                color = Color.White
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
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = largeText
                                )
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 150.dp),
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
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            val imageUrl = "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/${game.appid}/library_600x900.jpg"
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
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable {
                        viewModel.toggleFavoriteGame(game)
                    },
                tint = Color.Red
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = game.name,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = mediumText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.total_playtime, (game.playtime_forever / 60).toString()),
                            color = Color.LightGray,
                            fontSize = mediumSmallText
                        )
                    }
                }
            }
        }
    }
}
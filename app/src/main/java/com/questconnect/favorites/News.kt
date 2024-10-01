package com.questconnect.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.questconnect.games.Game
import com.questconnect.ui.theme.basicDimension
import com.questconnect.ui.theme.doubleBasicDimension
import com.questconnect.ui.theme.largeText
import com.questconnect.ui.theme.mediumSmallText
import com.questconnect.ui.theme.mediumText
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.questconnect.R
import com.questconnect.ui.theme.SteamMain
import com.questconnect.ui.theme.halfBasicDimension

@Composable
fun Favorites() {
    val viewModel = hiltViewModel<FavoritesViewModel>()
    val favoriteGames = viewModel.favoriteGames.collectAsState(initial = emptyList())
    val gameNews = viewModel.gameNews.collectAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.collectAsState(initial = false)

    var showNewsModal by remember { mutableStateOf(false) }
    var selectedGameNews by remember { mutableStateOf<List<NewsItem>>(emptyList()) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn() {
            items(favoriteGames.value) { game ->
                GameItem(game, onClick = {
                    viewModel.fetchGameNews(game.appid.toLong())
                    selectedGameNews = gameNews.value
                    showNewsModal = true
                })
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }

        if (isLoading.value) {
            LinearProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (showNewsModal && !isLoading.value) {
            NewsModal(newsItems = selectedGameNews, onDismiss = { showNewsModal = false })
        }
    }
}

@Composable
fun GameItem(game: Game, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(basicDimension))
            .background(color = SteamMain)
            .clickable(onClick = onClick)
            .padding(doubleBasicDimension)
    ) {
        Text(text = game.name, fontSize = mediumText)
    }
}

@Composable
fun NewsModal(newsItems: List<NewsItem>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.latest_news), fontSize = largeText) },
        text = {
            Column {
                newsItems.forEach { news ->
                    Text(text = news.title, fontSize = mediumText)
                    Spacer(modifier = Modifier.height(halfBasicDimension))
                    if (news.contents.isEmpty()){
                        Text(text = stringResource(id = R.string.no_news))
                    }
                    else {
                        Text(text = news.contents, fontSize = mediumSmallText)
                    }
                    Spacer(modifier = Modifier.height(basicDimension))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.close))
            }
        }
    )
}
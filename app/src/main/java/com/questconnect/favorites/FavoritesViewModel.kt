package com.questconnect.favorites
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.questconnect.apiManager.SteamApiServiceImpl
import com.questconnect.data.PreferencesKeys
import com.questconnect.data.QuestConnectDatabase
import com.questconnect.data.getFromDataStore
import com.questconnect.games.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: SteamApiServiceImpl
) : ViewModel() {

    private val _favoriteGames = MutableStateFlow(listOf<Game>())
    val favoriteGames: StateFlow<List<Game>> = _favoriteGames.asStateFlow()

    private val _gameNews = MutableStateFlow<List<NewsItem>>(emptyList())
    val gameNews: StateFlow<List<NewsItem>> = _gameNews.asStateFlow()

    private var steamId: String = ""

    private val questConnectDatabase = QuestConnectDatabase.getDatabase(context)

    private val faveGamesFlow = questConnectDatabase.favoriteDao().getAllFavoriteGames().asFlow()
    val favoriteGameIds = MutableStateFlow<Set<Long>>(emptySet())

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    init {
        loadSteamIdAndFavorites()
    }

    private fun loadSteamIdAndFavorites() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.STEAM_USER_ID_KEY).collect { id ->
                steamId = id ?: ""
                observeFavoriteGames()
            }
        }
    }

    private fun observeFavoriteGames() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.STEAM_USER_ID_KEY).collect { id ->
                faveGamesFlow.collect { favorites ->
                    val filteredFavorites = favorites.filter { it.userId.toString() == id }
                    favoriteGameIds.value = filteredFavorites.map { it.appId }.toSet()

                    loadFavoriteGames(favoriteGameIds.value.toList())
                }
            }
        }
    }

    private fun loadFavoriteGames(appIds: List<Long>) {
        if (appIds.isNotEmpty()) {
            apiServiceImpl.getOwnedGames(
                context = context,
                steamId = steamId,
                appIdsFilter = appIds.toString(),
                onSuccess = { response ->
                    viewModelScope.launch {
                        val gamesList = response.response.games

                        val updatedGamesList = gamesList.filter { game ->
                            appIds.contains(game.appid.toLong())
                        }

                        _favoriteGames.emit(updatedGamesList)
                    }
                },
                onFail = {
                    _snackbarMessage.value = "Failed to load favorite games"
                },
                loadingFinished = {}
            )
        }
    }

    fun fetchGameNews(appId: Long) {
        _isLoading.value = true
        apiServiceImpl.getNewsForGame(
            context = context,
            appId = appId,
            onSuccess = { response ->
                viewModelScope.launch {
                    _gameNews.emit(response.appnews.newsitems)
                    _isLoading.value = false
                }
            },
            onFail = {
                _isLoading.value = false
                _snackbarMessage.value = "Failed to fetch game news"
            },
            loadingFinished = {}
        )
    }
}
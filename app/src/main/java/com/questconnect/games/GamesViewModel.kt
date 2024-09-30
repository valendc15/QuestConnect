package com.questconnect.games

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asFlow
import com.questconnect.apiManager.SteamApiServiceImpl
import com.questconnect.data.Favorite
import com.questconnect.data.PreferencesKeys
import com.questconnect.data.QuestConnectDatabase
import com.questconnect.data.getFromDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: SteamApiServiceImpl,
) : ViewModel() {

    private val _loadingGames = MutableStateFlow(false)
    val loadingGames = _loadingGames.asStateFlow()

    private val _games = MutableStateFlow(listOf<Game>())
    val games = _games.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    private var steamId: String = ""

    private val questConnectDatabase = QuestConnectDatabase.getDatabase(context)

    private val faveGamesFlow = questConnectDatabase.favoriteDao().getAllFavoriteGames().asFlow()
    val favoriteGameIds = MutableStateFlow<Set<Long>>(emptySet())

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    init {
        loadSteamIdAndGames()
        observeFavoriteGames()
    }

    private fun loadSteamIdAndGames() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.STEAM_USER_ID_KEY).collect { id ->
                steamId = id ?: ""
                loadGames()
            }
        }
    }

    private fun observeFavoriteGames() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.STEAM_USER_ID_KEY).collect { id ->
                faveGamesFlow.collect { favorites ->
                    val filteredFavorites = favorites.filter { it.userId.toString() == id}

                    favoriteGameIds.value = if (filteredFavorites.isEmpty()) {
                        emptySet()
                    } else {
                        filteredFavorites.map { it.appId }.toSet()
                    }

                    _games.value = _games.value.map { game ->
                        game.copy(isFavorite = favoriteGameIds.value.contains(game.appid.toLong()))
                    }
                }
            }
        }
    }

    fun retryLoadingGames() {
        loadGames()
    }

    private fun loadGames() {
        _loadingGames.value = true
        apiServiceImpl.getOwnedGames(
            context = context,
            steamId = steamId,
            onSuccess = { response ->
                viewModelScope.launch {
                    val gamesList = response.response.games.sortedByDescending { it.name }

                    val updatedGamesList = gamesList.map { game ->
                        val isFavorite = favoriteGameIds.value.contains(game.appid.toLong())
                        game.copy(isFavorite = isFavorite)
                    }

                    _games.emit(updatedGamesList)
                }
                _showRetry.value = false
            },
            onFail = {
                _showRetry.value = true
            },
            loadingFinished = {
                _loadingGames.value = false
            }
        )
    }

    fun toggleFavoriteGame(game: Game) {
        viewModelScope.launch {
            try {
                if (game.isFavorite) {
                    questConnectDatabase.favoriteDao().deleteFavoriteByIdAndType(game.appid.toLong(), "SteamGames", steamId.toLong())
                    _snackbarMessage.value = "Removed game from favorites"
                } else {
                    questConnectDatabase.favoriteDao().insertFavorite(Favorite(appId = game.appid.toLong(), typeName = "SteamGames", userId = steamId.toLong()))
                    _snackbarMessage.value = "Added game to favorites"
                }

                val updatedGame = game.copy(isFavorite = !game.isFavorite)
                _games.value = _games.value.map {
                    if (it.appid == updatedGame.appid) {
                        updatedGame
                    } else {
                        it
                    }
                }
            } catch (e: Exception) {
                println("Error toggling favorite: ${e.message}")
            }
        }
    }
}
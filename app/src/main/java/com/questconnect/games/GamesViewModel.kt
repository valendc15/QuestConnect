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

    init {
        loadSteamIdAndGames()
        observeFavoriteGames()
    }


    private fun loadSteamIdAndGames() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.STEAM_USER_ID_KEY).collect { id ->
                steamId = id ?: ""
                println("Steam ID Loaded: $steamId")
                loadGames()
            }
        }
    }


    private fun observeFavoriteGames() {
        viewModelScope.launch {
            faveGamesFlow.collect { favorites ->
                println("Favorites Loaded: ${favorites.map { it.appId }}")


                favoriteGameIds.value = if (favorites.isEmpty()) {
                    emptySet()
                } else {
                    favorites.map { it.appId }.toSet()
                }


                _games.value = _games.value.map { game ->
                    game.copy(isFavorite = favoriteGameIds.value.contains(game.appid.toLong()))
                }

                println("Updated Games List after observing favorites: ${_games.value.map { it.appid to it.isFavorite }}")
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
                        println("Game: ${game.name}, isFavorite: $isFavorite")
                        game.copy(isFavorite = isFavorite)
                    }

                    _games.emit(updatedGamesList)

                    println("Games List after API fetch: ${_games.value.map { it.appid to it.isFavorite }}")
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
                    val rowsDeleted = questConnectDatabase.favoriteDao().deleteFavoriteById(game.appid.toLong())
                    if (rowsDeleted > 0) {
                        println("Successfully deleted game with appid: ${game.appid}")
                    } else {
                        println("Failed to delete game with appid: ${game.appid}")
                    }
                } else {
                    questConnectDatabase.favoriteDao().insertFavorite(Favorite(appId = game.appid.toLong(), typeName = "SteamGames"))
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
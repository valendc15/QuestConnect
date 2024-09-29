package com.questconnect.games

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.questconnect.apiManager.SteamApiServiceImpl
import com.questconnect.data.PreferencesKeys
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

    init {
        loadSteamIdAndGames()
    }

    private fun loadSteamIdAndGames() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.STEAM_USER_ID_KEY).collect { id ->
                steamId = id ?: ""
                loadGames()
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
            onSuccess = {
                viewModelScope.launch {
                    _games.emit(it.response.games.sortedByDescending { it.name })
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
}
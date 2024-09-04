package com.example.unscramble.ranking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.questconnect.apiManager.SteamApiServiceImpl
import com.questconnect.games.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: SteamApiServiceImpl,
): ViewModel() {

    private val _loadingGames = MutableStateFlow(false)
    val loadingRanking = _loadingGames.asStateFlow()

    private val _games = MutableStateFlow(listOf<Game>())
    val ranking = _games.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {
        loadRanking()
    }

    fun retryLoadingRanking() {
        loadRanking()
    }

    private fun loadRanking() {
        _loadingGames.value = true
        apiServiceImpl.getOwnedGames(
            context = context,
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
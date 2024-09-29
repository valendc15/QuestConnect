package com.questconnect.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.questconnect.apiManager.SteamApiServiceImpl
import com.questconnect.data.PreferencesKeys
import com.questconnect.data.getFromDataStore
import com.questconnect.data.saveToDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiServiceImpl: SteamApiServiceImpl
) : ViewModel() {

    private val _loadingSteamId = MutableStateFlow(false)
    val loadingSteamId = _loadingSteamId.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _steamId = MutableStateFlow("")
    val steamId = _steamId.asStateFlow()

    private val _showRetry = MutableStateFlow(false)
    val showRetry = _showRetry.asStateFlow()

    init {
        getUserNameFromDataStore()
    }

    private fun getUserNameFromDataStore() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.STEAM_USER_NAME_KEY).collect { savedUserName ->
                _userName.value = savedUserName ?: ""
                if (!savedUserName.isNullOrEmpty()) {
                    loadSteamId(savedUserName)
                }
            }
        }
    }

    private fun loadSteamId(userName: String) {
        _loadingSteamId.value = true
        apiServiceImpl.getSteamId(
            context = context,
            username = userName,
            onSuccess = { steamId ->
                viewModelScope.launch {
                    _steamId.emit(steamId)

                    saveSteamIdToDataStore(steamId)
                }
                _showRetry.value = false
            },
            onFail = {
                _showRetry.value = true
            },
            loadingFinished = {
                _loadingSteamId.value = false
            }
        )
    }

    private suspend fun saveSteamIdToDataStore(steamId: String) {
        saveToDataStore(context, steamId, PreferencesKeys.STEAM_USER_ID_KEY)
    }

    fun retryLoadingSteamId() {
        if (_userName.value.isNotEmpty()) {
            loadSteamId(_userName.value)
        }
    }

    fun saveUsernameToDataStorageAndGetSteamId(userName: String) {
        viewModelScope.launch {
            saveToDataStore(context, userName, PreferencesKeys.STEAM_USER_NAME_KEY)
            _userName.value = userName
            loadSteamId(userName)
        }
    }

    fun resetUsernameAndSteamID() {
        viewModelScope.launch {
            saveToDataStore(context, "", PreferencesKeys.STEAM_USER_NAME_KEY)
            _userName.value = ""

            saveToDataStore(context, "", PreferencesKeys.STEAM_USER_ID_KEY)
        }
    }
}
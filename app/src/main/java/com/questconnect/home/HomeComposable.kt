package com.questconnect.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.questconnect.R
import com.questconnect.ui.theme.SteamBlue
import com.questconnect.ui.theme.SteamLightBlue
import com.questconnect.ui.theme.mediumSmallText
import kotlinx.coroutines.flow.StateFlow


@Composable
fun Home() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val userName by viewModel.userName.collectAsState()
    val steamId by viewModel.steamId.collectAsState()

    if (steamId.isEmpty()) {
        LoginScreen(
            onLogin = { inputUserName ->
                viewModel.saveUsernameToDataStorageAndGetSteamId(inputUserName)
            },
            isLoading = viewModel.loadingSteamId,
            isLoginFailed = viewModel.loginFailed
        )
    } else {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = SteamBlue
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.welcome, userName),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Button(
                    onClick = { viewModel.resetUsernameAndSteamID() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(id = R.string.logout))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLogin: (String) -> Unit,
    isLoading: StateFlow<Boolean>,
    isLoginFailed: StateFlow<Boolean>
) {
    val isLoadingValue by isLoading.collectAsState()
    val isLoginFailedValue by isLoginFailed.collectAsState()
    var steamUsername by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SteamBlue)
            .padding(16.dp)
    ) {
        when {
            isLoadingValue -> {
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
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (isLoginFailedValue) {
                        Text(
                            text = stringResource(id = R.string.login_failed_help),
                            fontSize = mediumSmallText,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icons8_steam),
                            contentDescription = stringResource(id = R.string.steam_icon_descriptor),
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = steamUsername,
                            onValueChange = { steamUsername = it },
                            label = { Text(stringResource(id = R.string.steam_username)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = {
                            if (steamUsername.isNotEmpty()) {
                                onLogin(steamUsername)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.login))
                    }
                }
            }
        }
    }
}
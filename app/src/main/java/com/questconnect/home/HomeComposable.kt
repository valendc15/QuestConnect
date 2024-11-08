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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.questconnect.R
import com.questconnect.ui.theme.basicDimension
import com.questconnect.ui.theme.bigDimension
import com.questconnect.ui.theme.doubleBasicDimension
import com.questconnect.ui.theme.mediumBigDimension
import com.questconnect.ui.theme.mediumSmallText
import kotlinx.coroutines.flow.MutableStateFlow
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
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(doubleBasicDimension),
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
                    modifier = Modifier.padding(doubleBasicDimension),
                ) {
                    Text(stringResource(id = R.string.logout))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String) -> Unit = {},
    isLoading: StateFlow<Boolean> = MutableStateFlow(false),
    isLoginFailed: StateFlow<Boolean> = MutableStateFlow(false)
) {
    val isLoadingValue by isLoading.collectAsState()
    val isLoginFailedValue by isLoginFailed.collectAsState()
    var steamUsername by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(doubleBasicDimension)
    ) {
        when {
            isLoadingValue -> {
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
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = doubleBasicDimension)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icons8_steam),
                            contentDescription = stringResource(id = R.string.steam_icon_descriptor),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(mediumBigDimension)
                                .padding(end = basicDimension)
                        )
                        OutlinedTextField(
                            value = steamUsername,
                            onValueChange = { steamUsername = it },
                            label = { Text(stringResource(id = R.string.steam_username)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                cursorColor = MaterialTheme.colorScheme.onPrimary,
                                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                focusedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
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
                            .padding(top = doubleBasicDimension)
                    ) {
                        Text(text = stringResource(id = R.string.login))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}




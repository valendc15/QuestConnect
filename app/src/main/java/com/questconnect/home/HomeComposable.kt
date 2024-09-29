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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.questconnect.R
import com.questconnect.ui.theme.Pink80
import com.questconnect.ui.theme.QuestConnectTheme
import com.questconnect.ui.theme.SteamBlue
import com.questconnect.ui.theme.SteamLightBlue
import com.questconnect.ui.theme.largeText
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

data class HomeButton(
    val leadingIcon: ImageVector,
    val title: String,
    val trailingIcon: ImageVector,
    val onClick: () -> Unit,
    val color: Color
)

@Composable
fun Home() {
    val viewModel = hiltViewModel<HomeViewModel>()
    var showDialog by remember { mutableStateOf(false) }
    val userName by viewModel.userName.collectAsState()
    val steamId by viewModel.steamId.collectAsState()
    if (userName.isEmpty()) {

        LoginScreen(onLogin = { inputUserName ->
            viewModel.saveUsernameToDataStorageAndGetSteamId(inputUserName)
        },
            isLoading = viewModel.loadingSteamId,
            isLoginFailed = viewModel.showRetry,
            onRetry = { viewModel.retryLoadingSteamId() })
    }
    else {

        val buttons = listOf(
            HomeButton(
                leadingIcon = Icons.Default.Home,
                title = "Gay",
                trailingIcon = Icons.Default.ArrowForward,
                onClick = { viewModel.resetUsernameAndSteamID() },
                color = MaterialTheme.colorScheme.primary
            ),
            HomeButton(
                leadingIcon = Icons.Default.AccountCircle,
                title = userName,
                trailingIcon = Icons.Default.ArrowForward,
                onClick = { /* Handle Profile click */ },
                color = MaterialTheme.colorScheme.primary
            ),
            HomeButton(
                leadingIcon = Icons.Default.KeyboardArrowUp,
                title = steamId,
                trailingIcon = Icons.Default.ArrowForward,
                onClick = { showDialog = true },
                color = Pink80
            )
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
            //AnimatedVisibility(visible = showImage){
            //Image(painter = painterResource(id = R.drawable.nombre),
            // contentDescriptor= "",
            // contentScale = ContentScale.FillBounds,
            // modifier = Modifier.size(100.dp) }
        ) {
            Column {
                buttons.forEach { button ->
                    ButtonWithIcon(
                        leadingIcon = button.leadingIcon,
                        title = button.title,
                        trailingIcon = button.trailingIcon,
                        onClick = button.onClick,
                        color = button.color
                    )
                }
            }
            if (showDialog) {
                MyModalDialog(onDismiss = { showDialog = false })
            }
        }
    }
}

@Composable
fun ButtonWithIcon(
    leadingIcon: ImageVector,
    title: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    color: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(color)
    ) {
        androidx.compose.foundation.layout.Row {
            androidx.compose.material3.Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            androidx.compose.material3.Icon(
                imageVector = trailingIcon,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MyModalDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(text = "My Modal Dialog")
        },
        text = {
            Text("This is the content of the modal dialog.")
        }
    )
}



@Composable
fun LoginScreen(
    onLogin: (String) -> Unit,
    onRetry: () -> Unit,
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
            isLoginFailedValue -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login Failed",
                        fontSize = largeText,
                        color = Color.White
                    )
                    Text(
                        text = "Please check your username and try again.",
                        color = Color.White
                    )
                    Button(onClick = onRetry) {
                        Text(text = "Retry")
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Steam Icon
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

@Preview
@Composable
fun PreviewHome() {
    QuestConnectTheme {
        Home()
    }
}

package com.questconnect

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.questconnect.data.QuestConnectDatabase
import com.questconnect.navigation.BottomBar
import com.questconnect.navigation.NavHostComposable
import com.questconnect.security.BiometricAuthManager
import com.questconnect.ui.theme.QuestConnectTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity
    () {

    @Inject
    lateinit var questConnectDatabase: QuestConnectDatabase

    @Inject
    lateinit var biometricAuthManager: BiometricAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isAuthenticated by remember { mutableStateOf(false) }
            if (isAuthenticated) {
                val navController = rememberNavController()
                QuestConnectTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val scope = rememberCoroutineScope()

                        Scaffold(
                            bottomBar = {
                                BottomBar { navController.navigate(it) }
                            }
                        ) { innerPadding ->
                            NavHostComposable(innerPadding, navController)
                        }

                    }
                }
            }
            else {
                BiometricAuthentication(
                    isAuthenticated,
                    onSuccess = { isAuthenticated = true },
                    biometricAuthManager,
                )
            }
        }
    }

    @Composable
    fun BiometricAuthentication(
        isAuthenticated: Boolean,
        onSuccess: () -> Unit,
        biometricAuthManager: BiometricAuthManager
    ) {
        val context = LocalContext.current
        val biometricManager = remember { BiometricManager.from(context) }
        val isBiometricAvailable = remember {
            biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        }
        when (isBiometricAvailable) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                if (!isAuthenticated) {
                    biometricAuthManager.authenticate(context, {}, onSuccess, {})
                }
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Text(text = stringResource(id = R.string.biometric_error_no_hardware))
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Text(text = stringResource(id = R.string.biometric_error_hw_not_available))
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Text(text = stringResource(id = R.string.biometric_error_security_update_required))
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Text(text = stringResource(id = R.string.biometric_error_unsuported))
            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Text(text = stringResource(id = R.string.biometric_error_other))
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Text(text = stringResource(id = R.string.biometric_error_other))
            }
        }
    }

}


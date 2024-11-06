package com.questconnect.security
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.questconnect.R
import javax.inject.Inject
class BiometricAuthManager @Inject constructor() {
    @Composable
    fun authenticate(context: Context, onError: () -> Unit, onSuccess: () -> Unit, onFail: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError()
                }
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFail()
                }
            }
        )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setTitle(stringResource(id = R.string.biometric_authentication))
            .setSubtitle(stringResource(id = R.string.login_biometrics))
            .setNegativeButtonText(stringResource(id = R.string.cancel))
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}
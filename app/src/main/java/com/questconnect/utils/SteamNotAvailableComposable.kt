package com.questconnect.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.questconnect.R
import com.questconnect.ui.theme.basicDimension
import com.questconnect.ui.theme.bigDimension
import com.questconnect.ui.theme.doubleBasicDimension

@Composable
public fun SteamNotAvailable(stringId: Int) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(doubleBasicDimension),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icons8_steam),
                contentDescription = stringResource(id = R.string.steam_icon_descriptor),
                modifier = Modifier.size(bigDimension),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(doubleBasicDimension))
            Text(
                text = stringResource(stringId),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

package com.questconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.questconnect.ui.theme.Pink80
import com.questconnect.ui.theme.QuestConnectTheme

data class HomeButton(
    val leadingIcon: ImageVector,
    val title: String,
    val trailingIcon: ImageVector,
    val onClick: () -> Unit,
    val color: Color
)

@Composable
fun Home() {
    var showDialog by remember { mutableStateOf(false) }

    val buttons = listOf(
        HomeButton(
            leadingIcon = Icons.Default.Home,
            title = "Home",
            trailingIcon = Icons.Default.ArrowForward,
            onClick = { /* Handle Home click */ },
            color = MaterialTheme.colorScheme.primary
        ),
        HomeButton(
            leadingIcon = Icons.Default.AccountCircle,
            title = "Profile",
            trailingIcon = Icons.Default.ArrowForward,
            onClick = { /* Handle Profile click */ },
            color= MaterialTheme.colorScheme.primary
        ),
        HomeButton(
            leadingIcon = Icons.Default.KeyboardArrowUp,
            title = "Open",
            trailingIcon = Icons.Default.ArrowForward,
            onClick = {showDialog= true},
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

@Preview
@Composable
fun PreviewHome() {
    QuestConnectTheme {
        Home()
    }
}

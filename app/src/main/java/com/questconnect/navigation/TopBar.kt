package com.questconnect.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopBar(onMenuClick: () -> Unit) {
//    TopAppBar(
//        title = {
//            Text(
//                text = "Quest Connect",
//                style = MaterialTheme.typography.labelSmall,
//                color = Color.White
//            )
//        },
//        navigationIcon = {
//            IconButton(onClick = onMenuClick) {
//                Icon(
//                    imageVector = Icons.Filled.Menu,
//                    contentDescription = "Menu",
//                    tint = Color.White
//                )
//            }
//        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = SteamMain,
//            titleContentColor = Color.White,
//            navigationIconContentColor = Color.White
//        )
//    )
//}
//@Composable
//fun DrawerContent(onDestinationClicked: (route: String) -> Unit) {
//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxSize()
//    ) {
//        Text(QuestConnectScreen.Home.name, modifier = Modifier.clickable { onDestinationClicked("home") })
//    }
//}
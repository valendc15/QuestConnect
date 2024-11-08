package com.questconnect
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.EaseInOut
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.slideInHorizontally
//import androidx.compose.animation.slideOutHorizontally
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.Close
//import androidx.compose.material.icons.rounded.Menu
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import com.questconnect.ui.theme.SteamLightBlue
//import kotlinx.coroutines.delay
//
//@Composable
//fun SideBar() {
//    var showMenu by remember { mutableStateOf(false) }
//    var showHamburgButton by remember { mutableStateOf(true) }
//
//    Surface(modifier = Modifier.fillMaxWidth()) {
//        Row {
//            if (showHamburgButton && !showMenu) {
//                HamburgButton(onClick = { showMenu = true })
//            }
//
//            AnimatedVisibility(
//                visible = showMenu,
//                enter = slideInHorizontally(
//                    initialOffsetX = { -it },
//                    animationSpec = tween(
//                        durationMillis = 500,
//                        easing = EaseInOut
//                    )
//                ),
//                exit = slideOutHorizontally(
//                    targetOffsetX = { -it },
//                    animationSpec = tween(
//                        durationMillis = 500,
//                        easing = EaseInOut
//                    )
//                )
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth(0.4f)
//                        .fillMaxHeight()
//                        .background(Color.LightGray)
//                        .padding(top = 20.dp)
//                ) {
//                    CloseButton(
//                        onClick = {
//                            showMenu = false
//                            showHamburgButton = false
//                        },
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .padding(8.dp)
//                    )
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(start = 16.dp, top = 48.dp),
//                        verticalArrangement = Arrangement.Top,
//                        horizontalAlignment = Alignment.Start
//                    ) {
//                        Text(
//                            text = stringResource(id = R.string.menu_item_one),
//                            color = Color.Black,
//                            modifier = Modifier.padding(horizontal = 8.dp)
//                        )
//                        Text(
//                            text = stringResource(id = R.string.menu_item_two),
//                            color = Color.Black,
//                            modifier = Modifier.padding(horizontal = 8.dp)
//                        )
//                    }
//                }
//            }
//
//            LaunchedEffect(showMenu) {
//                if (!showMenu) {
//                    delay(500) // Wait for the exit animation to finish
//                    showHamburgButton = true
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun HamburgButton(onClick: () -> Unit) {
//    Icon(
//        Icons.Rounded.Menu,
//        contentDescription = stringResource(id = R.string.menu_icon_descriptor),
//        tint = SteamLightBlue,
//        modifier = Modifier
//            .clickable { onClick() }
//            .padding(vertical = 40.dp, horizontal = 20.dp).size(50.dp)
//    )
//}
//
//@Composable
//fun CloseButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
//    Icon(
//        Icons.Rounded.Close,
//        contentDescription = stringResource(id = R.string.close_icon_descriptor),
//        tint = Color.Black,
//        modifier = modifier.clickable { onClick() }
//    )
//}

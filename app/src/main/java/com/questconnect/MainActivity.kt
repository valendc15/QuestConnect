package com.questconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.questconnect.data.ContentTypes
import com.questconnect.data.QuestConnectDatabase
import com.questconnect.navigation.BottomBar
import com.questconnect.navigation.DrawerContent
import com.questconnect.navigation.NavHostComposable
import com.questconnect.navigation.TopBar
import com.questconnect.ui.theme.QuestConnectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var questConnectDatabase: QuestConnectDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDefaultContentTypes()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            QuestConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        drawerContent = {
                            DrawerContent { route ->
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(route)
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            topBar = {
                                TopBar(
                                    onMenuClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                )
                            },
                            bottomBar = {
                                BottomBar { navController.navigate(it) }
                            }
                        ) { innerPadding ->
                            NavHostComposable(innerPadding, navController)
                        }
                    }
                }
            }
        }
    }
    private fun addDefaultContentTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            val contentTypeExists = questConnectDatabase.contentTypesDao().getContentTypeByName("SteamGames") != null
            if (!contentTypeExists) {
                questConnectDatabase.contentTypesDao().insert(ContentTypes(0, "SteamGames"))
            }
        }
    }
}


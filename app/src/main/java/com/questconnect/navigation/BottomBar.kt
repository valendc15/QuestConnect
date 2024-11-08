package com.questconnect.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.questconnect.R

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val text: String
)

@Composable
fun BottomBar(
    onNavigate: (String) -> Unit,
) {

    val homeTab = TabBarItem(
    title = QuestConnectScreen.Home.name,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        text = stringResource(id = R.string.home)
    )
    val settingsTab = TabBarItem(
        title = QuestConnectScreen.Favorites.name,
        selectedIcon = Icons.Default.Favorite,
        unselectedIcon = Icons.Outlined.Favorite,
        text = stringResource(id = R.string.favorites)
    )
    val moreTab = TabBarItem(
        title = QuestConnectScreen.Games.name,
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons.Outlined.List,
                text = stringResource(id = R.string.games)
    )

    val tabBarItems = listOf(homeTab, settingsTab, moreTab)

    TabView(tabBarItems, onNavigate)
}

@Composable
fun TabView(tabBarItems: List<TabBarItem>, onNavigate: (String) -> Unit) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            val isSelected = selectedTabIndex == index
            val itemColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onPrimaryContainer
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedTabIndex = index
                    onNavigate(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = isSelected,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.text,
                        iconColor = itemColor
                    )
                },
                label = {
                    Text(
                        text = tabBarItem.text,
                        style = MaterialTheme.typography.labelSmall,
                        color = itemColor,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    iconColor: Color
) {
    Icon(
        imageVector = if (isSelected) selectedIcon else unselectedIcon,
        contentDescription = title,
        tint = iconColor
    )
}
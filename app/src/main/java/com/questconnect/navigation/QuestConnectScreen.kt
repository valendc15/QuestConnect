package com.questconnect.navigation


enum class QuestConnectScreen {
    Home,
    Games,
    Favorites,
}

val basePages = listOf(
    QuestConnectScreen.Home.name,
    QuestConnectScreen.Games.name,
    QuestConnectScreen.Favorites.name
)
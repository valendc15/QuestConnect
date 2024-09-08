package com.questconnect.navigation


enum class QuestConnectScreen {
    Home,
    Games,
    Edit,
}

val basePages = listOf(
    QuestConnectScreen.Home.name,
    QuestConnectScreen.Games.name,
    QuestConnectScreen.Edit.name
)
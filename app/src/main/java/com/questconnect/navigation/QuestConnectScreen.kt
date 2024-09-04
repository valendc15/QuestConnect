package com.questconnect.navigation


enum class QuestConnectScreen {
    Home,
    Library,
    Edit,
}

val basePages = listOf(
    QuestConnectScreen.Home.name,
    QuestConnectScreen.Library.name,
    QuestConnectScreen.Edit.name
)
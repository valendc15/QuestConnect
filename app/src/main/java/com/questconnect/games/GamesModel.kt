package com.questconnect.games

data class GamesModel(
    val game_count: Number,
    val games: List<Game>
)

data class Game(
    val name: String,
    val playtime_forever: Number,
    val img_icon_url: String
)


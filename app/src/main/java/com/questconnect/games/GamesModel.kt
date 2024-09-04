package com.questconnect.games

data class GameResponse(
    val response: OwnedGamesModel
)

data class OwnedGamesModel(
    val game_count: Int,
    val games: List<Game>
)

data class Game(
    val appid: Int,
    val name: String,
    val playtime_forever: Int,
    val img_icon_url: String
)

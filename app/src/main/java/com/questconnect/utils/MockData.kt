package com.questconnect.utils

import com.questconnect.favorites.NewsItem
import com.questconnect.games.Game

 public val mockedGames = listOf(
    Game(
        appid = 1,
        name = "The Witcher 3: Wild Hunt",
        playtime_forever = 120,
        img_icon_url = "https://example.com/images/witcher3_icon.png",
        isFavorite = true
    ),
    Game(
        appid = 2,
        name = "Cyberpunk 2077",
        playtime_forever = 80,
        img_icon_url = "https://example.com/images/cyberpunk2077_icon.png",
        isFavorite = false
    ),
    Game(
        appid = 3,
        name = "Half-Life: Alyx",
        playtime_forever = 50,
        img_icon_url = "https://example.com/images/halflifealyx_icon.png",
        isFavorite = true
    ),
    Game(
        appid = 4,
        name = "Stardew Valley",
        playtime_forever = 200,
        img_icon_url = "https://example.com/images/stardewvalley_icon.png",
        isFavorite = false
    ),
    Game(
        appid = 5,
        name = "DOOM Eternal",
        playtime_forever = 70,
        img_icon_url = "https://example.com/images/doometernal_icon.png",
        isFavorite = true
    ),
    Game(
        appid = 6,
        name = "Hades",
        playtime_forever = 40,
        img_icon_url = "https://example.com/images/hades_icon.png",
        isFavorite = false
    ),
    Game(
        appid = 7,
        name = "Minecraft",
        playtime_forever = 500,
        img_icon_url = "https://example.com/images/minecraft_icon.png",
        isFavorite = true
    ),
    Game(
        appid = 8,
        name = "The Elder Scrolls V: Skyrim",
        playtime_forever = 150,
        img_icon_url = "https://example.com/images/skyrim_icon.png",
        isFavorite = false
    )
)

val mockedNewsItems = listOf(
    NewsItem(
        title = "The Witcher 3: Wild Hunt - New Patch Released",
        url = "https://example.com/news/cyberpunk2077-dlc",
        contents = "The new patch for The Witcher 3 introduces several bug fixes and performance improvements.",
        date = 1699574400000L // Represents a timestamp (e.g., Nov 8, 2024)
    ),
    NewsItem(
        title = "Cyberpunk 2077 - Upcoming DLC Announced",
        url = "https://example.com/news/cyberpunk2077-dlc",
        contents = "Cyberpunk 2077's new DLC will bring new story content, areas to explore, and more customization options.",
        date = 1699488000000L // Represents a timestamp (e.g., Nov 7, 2024)
    ),
    NewsItem(
        title = "Half-Life: Alyx - Developer Update",
        url = "https://example.com/news/halflifealyx-dev-update",
        contents = "A new developer update for Half-Life: Alyx gives us insight into the future of VR in the franchise.",
        date = 1699401600000L // Represents a timestamp (e.g., Nov 6, 2024)
    ),
    NewsItem(
        title = "Minecraft - Halloween Event Highlights",
        url = "https://example.com/news/minecraft-halloween-event",
        contents = "Minecraft's Halloween event was a huge success, with limited-time items and special in-game activities.",
        date = 1699315200000L // Represents a timestamp (e.g., Nov 5, 2024)
    ),
    NewsItem(
        title = "Stardew Valley - New Update Coming Soon",
        url = "https://example.com/news/stardewvalley-update",
        contents = "The upcoming Stardew Valley update will bring new crops, animals, and expanded features.",
        date = 1699228800000L // Represents a timestamp (e.g., Nov 4, 2024)
    )
)

val mockedGamesWithPlaceholders = listOf(
    Game(
        appid = 1,
        name = "The Witcher 3: Wild Hunt",
        playtime_forever = 120,
        img_icon_url = "https://via.placeholder.com/150/0000FF/808080?Text=The+Witcher+3",
        isFavorite = true
    ),
    Game(
        appid = 2,
        name = "Cyberpunk 2077",
        playtime_forever = 80,
        img_icon_url = "https://via.placeholder.com/150/FF5733/FFFFFF?Text=Cyberpunk+2077",
        isFavorite = false
    ),
    Game(
        appid = 3,
        name = "Half-Life: Alyx",
        playtime_forever = 50,
        img_icon_url = "https://via.placeholder.com/150/8E44AD/FFFFFF?Text=Half-Life+Alyx",
        isFavorite = true
    ),
    Game(
        appid = 4,
        name = "Stardew Valley",
        playtime_forever = 200,
        img_icon_url = "https://via.placeholder.com/150/2ECC71/FFFFFF?Text=Stardew+Valley",
        isFavorite = false
    ),
    Game(
        appid = 5,
        name = "DOOM Eternal",
        playtime_forever = 70,
        img_icon_url = "https://via.placeholder.com/150/E74C3C/FFFFFF?Text=DOOM+Eternal",
        isFavorite = true
    ),
    Game(
        appid = 6,
        name = "Hades",
        playtime_forever = 40,
        img_icon_url = "https://via.placeholder.com/150/3498DB/FFFFFF?Text=Hades",
        isFavorite = false
    ),
    Game(
        appid = 7,
        name = "Minecraft",
        playtime_forever = 500,
        img_icon_url = "https://via.placeholder.com/150/F39C12/FFFFFF?Text=Minecraft",
        isFavorite = true
    ),
    Game(
        appid = 8,
        name = "The Elder Scrolls V: Skyrim",
        playtime_forever = 150,
        img_icon_url = "https://via.placeholder.com/150/1ABC9C/FFFFFF?Text=Skyrim",
        isFavorite = false
    )
)
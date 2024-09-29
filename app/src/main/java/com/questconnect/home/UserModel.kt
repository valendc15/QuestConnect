package com.questconnect.home

data class SteamIdResponse (
    val response: SteamIdResponseData
)

data class SteamIdResponseData(
    val steamid: String?,
    val success: Int
)
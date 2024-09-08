package com.questconnect.apiManager
import com.questconnect.games.GameResponse
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

interface SteamApiService {

    @GET("IPlayerService/GetOwnedGames/v0001/")
     fun getOwnedGames(
        @Query("key") apiKey: String,
        @Query("steamid") steamId: String,
        @Query("format") format: String = "json",
        @Query("include_appinfo") includeAppInfo: Boolean = true
    ): Call<GameResponse>
}
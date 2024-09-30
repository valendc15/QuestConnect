package com.questconnect.apiManager
import com.questconnect.favorites.NewsResponse
import com.questconnect.games.GameResponse
import com.questconnect.home.SteamIdResponse
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
        @GET("ISteamUser/ResolveVanityURL/v1/")
        fun getSteamIdFromVanityUrl(
            @Query("key") apiKey: String,
            @Query("vanityurl") username: String
        ): Call<SteamIdResponse>

    @GET("ISteamNews/GetNewsForApp/v0002/")
    fun getNewsForApp(
        @Query("appid") appId: Long,
        @Query("count") count: Int = 3,
        @Query("maxlength") maxLength: Int = 300,
        @Query("format") format: String = "json"
    ): Call<NewsResponse>
}

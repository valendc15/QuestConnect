package com.questconnect.apiManager

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.questconnect.R
import com.questconnect.favorites.NewsResponse
import com.questconnect.games.GameResponse
import com.questconnect.home.SteamIdResponse
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject

class SteamApiServiceImpl @Inject constructor() {

    private val TAG = "SteamApiServiceImpl"

    fun getOwnedGames(
        context: Context,
        onSuccess: (GameResponse) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit,
        steamId: String
    ) {

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.steam_api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SteamApiService = retrofit.create(SteamApiService::class.java)
        println("Este es el steamId: " + steamId)

        val call: Call<GameResponse> = service.getOwnedGames(
            apiKey = context.getString(R.string.steam_api_key),
            steamId = steamId
        )

        call.enqueue(object : Callback<GameResponse> {
            override fun onResponse(response: Response<GameResponse>?, retrofit: Retrofit?) {

                loadingFinished()
                if (response?.isSuccess == true && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure(Exception("Bad Request"))
                }
            }

            override fun onFailure(t: Throwable?) {
                Log.e(TAG, "API call failed", t)
                Toast.makeText(context, "Can't get owned games: ${t?.message}", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }

    fun getSteamId(
        context: Context,
        username: String,
        onSuccess: (String) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.steam_api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SteamApiService = retrofit.create(SteamApiService::class.java)

        val call: Call<SteamIdResponse> = service.getSteamIdFromVanityUrl(
            apiKey = context.getString(R.string.steam_api_key),
            username = username
        )

        call.enqueue(object : Callback<SteamIdResponse> {
            override fun onResponse(response: Response<SteamIdResponse>?, retrofit: Retrofit?) {
                loadingFinished()
                if (response?.isSuccess == true && response.body() != null && response.body()?.response?.success == 1) {
                    val steamId = response.body()?.response?.steamid
                    if (steamId != null) {
                        onSuccess(steamId)
                    } else {
                        onFailure(Exception("Steam ID not found"))
                    }
                } else {
                    onFailure(Exception("Failed to resolve Steam ID"))
                }
            }

            override fun onFailure(t: Throwable?) {
                Log.e(TAG, "API call failed", t)
                Toast.makeText(context, "Can't resolve Steam ID: ${t?.message}", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
    fun getNewsForGame(
        context: Context,
        appId: Long,
        onSuccess: (NewsResponse) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.steam_api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SteamApiService = retrofit.create(SteamApiService::class.java)

        val call: Call<NewsResponse> = service.getNewsForApp(appId)

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(response: Response<NewsResponse>?, retrofit: Retrofit?) {
                loadingFinished()
                if (response?.isSuccess == true && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onFailure(Exception("Failed to fetch news"))
                }
            }

            override fun onFailure(t: Throwable?) {
                Log.e(TAG, "API call failed", t)
                Toast.makeText(context, "Can't get news: ${t?.message}", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}

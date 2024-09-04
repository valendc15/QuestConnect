package com.questconnect.apiManager

import android.content.Context
import android.widget.Toast
import com.questconnect.R
import com.questconnect.games.GameResponse
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject

class SteamApiServiceImpl @Inject constructor() {

    fun getOwnedGames(context: Context, onSuccess: (GameResponse) -> Unit, onFail: () -> Unit, loadingFinished: () -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(
                context.getString(R.string.steam_api_url)
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        val service: SteamApiService= retrofit.create(SteamApiService::class.java)

        val call: Call<GameResponse> = service.getOwnedGames(apiKey = R.string.steam_api_key.toString(), steamId = R.string.steam_user_id.toString())

        call.enqueue(object : Callback<GameResponse> {
            override fun onResponse(response: Response<GameResponse>?, retrofit: Retrofit?) {
                loadingFinished()
                if(response?.isSuccess == true) {
                    val jokes: GameResponse = response.body()
                    onSuccess(jokes)
                } else {
                    onFailure(Exception("Bad request"))
                }
            }

            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, "Can't get owned games", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}
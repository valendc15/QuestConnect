package com.questconnect.apiManager

import android.content.Context
import android.util.Log
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

    private val TAG = "SteamApiServiceImpl"

    fun getOwnedGames(
        context: Context,
        onSuccess: (GameResponse) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        Log.d(TAG, "Creating Retrofit instance")

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.steam_api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SteamApiService = retrofit.create(SteamApiService::class.java)

        Log.d(TAG, "Preparing API call")

        val call: Call<GameResponse> = service.getOwnedGames(
            apiKey = context.getString(R.string.steam_api_key),
            steamId = context.getString(R.string.steam_user_id)
        )

        Log.d(TAG, "Executing API call")

        call.enqueue(object : Callback<GameResponse> {
            override fun onResponse(response: Response<GameResponse>?, retrofit: Retrofit?) {
                Log.d(TAG, "API call response received")

                loadingFinished()
                if (response?.isSuccess == true && response.body() != null) {
                    Log.d(TAG, "API call successful: ${response.body()}")
                    onSuccess(response.body()!!)
                } else {
                    Log.e(TAG, "API call failed: ${response?.errorBody()?.string()}")
                    onFail()
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
}
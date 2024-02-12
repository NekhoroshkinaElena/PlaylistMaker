package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.Requester
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitRequester : Requester {

    private val trackBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create<TrackApiService>()
    override fun doRequest(dto: Any): Response {
        return try {
            if (dto is TrackRequest) {
                val response = trackService.search(dto.text).execute()

                val body = response.body() ?: Response()

                body.apply { resultCode = response.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            Log.i("TAG", "doRequest: " + "Попали сюда")
            return Response()
        }
    }
}

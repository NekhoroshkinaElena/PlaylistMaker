package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.Requester
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackRequest

class RetrofitRequester(
    private val trackApiService: TrackApiService,
    private val context: Context
) : Requester {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return try {
            if (dto is TrackRequest) {
                val response = trackApiService.search(dto.text).execute()

                val body = response.body() ?: Response()

                body.apply { resultCode = response.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            return Response()
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}

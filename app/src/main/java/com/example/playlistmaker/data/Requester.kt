package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response

interface Requester {

    fun doRequest(dto: Any): Response
}

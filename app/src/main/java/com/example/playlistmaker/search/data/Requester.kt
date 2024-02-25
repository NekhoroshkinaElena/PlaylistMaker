package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response

interface Requester {

    fun doRequest(dto: Any): Response
}

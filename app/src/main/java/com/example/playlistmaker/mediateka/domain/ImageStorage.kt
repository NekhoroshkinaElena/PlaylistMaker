package com.example.playlistmaker.mediateka.domain

import android.net.Uri

interface ImageStorage {

    fun saveImageToPrivateStorage(uri: Uri): String

    fun getImageFromPrivateStorage(imageName: String): Uri
}

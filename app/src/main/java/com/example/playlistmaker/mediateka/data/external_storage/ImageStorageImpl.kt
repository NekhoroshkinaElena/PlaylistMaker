package com.example.playlistmaker.mediateka.data.external_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.mediateka.domain.ImageStorage
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class ImageStorageImpl(private val context: Context) : ImageStorage {
    override fun saveImageToPrivateStorage(uri: Uri): String {
        val filePath =
            File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myPlaylist"
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val imageName = Calendar.getInstance().time.toString()

        val file = File(filePath, imageName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return imageName
    }

    override fun getImageFromPrivateStorage(imageName: String): Uri {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "myPlaylist"
        )
        val file = File(filePath, imageName)
        return file.toUri()
    }
}

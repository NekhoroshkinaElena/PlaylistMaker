package com.example.playlistmaker.mediateka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mediateka.data.db.dao.PlaylistDao
import com.example.playlistmaker.mediateka.data.db.dao.TrackPlaylistDao
import com.example.playlistmaker.mediateka.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.data.db.entity.TrackPlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class, TrackPlaylistEntity::class])
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    abstract fun trackPlaylistDao(): TrackPlaylistDao
}

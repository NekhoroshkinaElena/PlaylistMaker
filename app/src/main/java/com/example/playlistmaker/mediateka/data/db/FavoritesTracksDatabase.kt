package com.example.playlistmaker.mediateka.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mediateka.data.db.dao.TrackDao
import com.example.playlistmaker.mediateka.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavoritesTracksDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}

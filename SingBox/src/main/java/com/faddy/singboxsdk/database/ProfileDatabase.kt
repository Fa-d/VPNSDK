package com.faddy.singboxsdk.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.faddy.singboxsdk.database.Profile

@Database(
    entities = [Profile::class], version = 1
)
abstract class ProfileDatabase : RoomDatabase() {

    abstract fun profileDao(): Profile.Dao

}
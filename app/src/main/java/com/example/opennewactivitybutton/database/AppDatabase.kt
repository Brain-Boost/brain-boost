package com.example.opennewactivitybutton.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.opennewactivitybutton.database.daos.AlarmDao
import com.example.opennewactivitybutton.database.entities.Alarm

@Database(entities = [Alarm::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AlarmDao(): AlarmDao
}

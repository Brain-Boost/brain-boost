package com.example.opennewactivitybutton.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.opennewactivitybutton.database.entities.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarm WHERE label = :label")
    fun getAlarmById(label: String): Alarm

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarm: Alarm)
}

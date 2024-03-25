package com.example.opennewactivitybutton.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.opennewactivitybutton.database.entities.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarm WHERE label = :label")
    suspend fun getAlarmById(label: String): Alarm?

    @Query("SELECT * FROM Alarm")
    fun getAllAlarms(): List<Alarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}

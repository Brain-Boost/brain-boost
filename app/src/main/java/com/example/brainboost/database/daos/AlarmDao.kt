package com.example.brainboost.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.brainboost.database.entities.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM Alarm WHERE label = :label")
    suspend fun getAlarmById(label: String): Alarm?

    @Query("SELECT * FROM Alarm")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM Alarm ORDER BY label DESC LIMIT 1")
    fun getLatestAlarm(): Flow<Alarm?>

    @Query("SELECT * FROM Alarm WHERE label = :label LIMIT 1")
    fun getAlarmByLabel(label: String): Flow<Alarm?>

    


}

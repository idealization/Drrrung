package cau.dururung.dururung.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.concurrent.Flow

@Dao
interface SleepDataDao {
    @Insert
    fun insert(sleepData: SleepData)

    @Delete
    fun delete(sleepData: SleepData)

    @Query("SELECT * FROM SleepData LIMIT 7")
    fun getWeek(): List<SleepData>

}
package cau.dururung.dururung.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class SleepData(
    var start_time: String,
    var end_time: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
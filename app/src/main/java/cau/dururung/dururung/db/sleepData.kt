package cau.dururung.dururung.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class SleepData(
    var sleep_date : String,
    var wakeUp_date : String,
    var start_time: String,
    var end_time: String,
    var rating: Float?
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
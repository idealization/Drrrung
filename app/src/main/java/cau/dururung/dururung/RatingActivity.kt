package cau.dururung.dururung

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cau.dururung.dururung.databinding.ActivityRatingBinding
import cau.dururung.dururung.db.SleepData
import cau.dururung.dururung.db.SleepDataDao
import cau.dururung.dururung.db.SleepDatabase


class RatingActivity : AppCompatActivity() {
    private lateinit var sleepDao: SleepDataDao
    private lateinit var binding: ActivityRatingBinding
    var ratingValue: Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingValue = rating
        }
        binding.save.setOnClickListener {
            val slpData = intent.getStringArrayExtra("sleepData")

            // 데이터저장

            accessDatabase()

            var entity = SleepData(
                sleep_date = slpData?.get(0).toString(),
                start_time = slpData?.get(1).toString(),
                wakeUp_date = slpData?.get(2).toString(),
                end_time = slpData?.get(3).toString(),
                rating = ratingValue
            )
            sleepDao.insert(entity)
            Log.d("time", slpData?.get(1).toString() + ", " + slpData?.get(3).toString())
            Toast.makeText(this, "${ratingValue}점", Toast.LENGTH_SHORT).show()
        }
    }
    private fun accessDatabase() {
        val db = SleepDatabase.getInstance(this)!!
        sleepDao = db.sleepDao()
    }
}

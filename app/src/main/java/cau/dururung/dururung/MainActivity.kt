package cau.dururung.dururung

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import cau.dururung.dururung.databinding.ActivityMainBinding
import cau.dururung.dururung.databinding.ActivityRatingBinding
import cau.dururung.dururung.db.SleepData
import cau.dururung.dururung.db.SleepDataDao
import cau.dururung.dururung.db.SleepDatabase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sleepDao: SleepDataDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        val db = Room.databaseBuilder(
            applicationContext,
            SleepDatabase::class.java, "database-name"
        ).build()
        */

        accessDatabase()
        // 임의로 데이터 삽입
        Thread(insertSleep).start()

        binding.button.setOnClickListener {
            Toast.makeText(this,"${sleepDao.getWeek()}", Toast.LENGTH_LONG).show()
        }





        val entries = ArrayList<CandleEntry>()
        entries.add(CandleEntry(0f,0f,0f,-1f,3.5f ))
        entries.add(CandleEntry(1f,0f,0f,-3f,3.5f ))
        entries.add(CandleEntry(2f,0f,0f,0f,2f ))
        entries.add(CandleEntry(3f,0f,0f,1f,4f ))
        entries.add(CandleEntry(4f,0f,0f,-2f,3f ))
        entries.add(CandleEntry(5f,0f,0f,2f,2.5f ))
        entries.add(CandleEntry(6f,0f,0f,2f,3.5f ))

        val dataSet = CandleDataSet(entries, "Sleep Data").apply {
            // 심지 부분
            //shadowColor = Color.LTGRAY
            //shadowWidth = 0F
            // 양봉
            increasingColor = ColorTemplate.rgb("#ff7b22")
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = Color.DKGRAY
            setDrawValues(false)
            barSpace = 1f
            // 터치시 노란 선 제거
            highLightColor = Color.TRANSPARENT
        }
        binding.chart.apply {
            this.data = CandleData(dataSet)


            //dataSet.formLineWidth = 0.35f
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            xAxis.labelCount = 7
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.axisMaximum = 5f
            axisRight.axisMaximum = 5f
            animateXY(0,800)

            axisRight.setDrawLabels(false)
            xAxis.setDrawGridLines(false)

            setScaleEnabled(false)
            setPinchZoom(false)


            description.isEnabled = false
            isHighlightPerDragEnabled = true
            requestDisallowInterceptTouchEvent(true)

            val labels = arrayOf("월", "화", "수","목","금","토","일")
            this.xAxis.valueFormatter = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return labels[value.toInt()]
                }
            }
            invalidate()
        }
    }

    private fun accessDatabase() {
        val db = SleepDatabase.getInstance(this)!!
        sleepDao = db.sleepDao()
    }

    private val insertSleep = Runnable {

        var entity = SleepData(
            start_time = "2021-07-19-01-21",
            end_time = "2021-07-19-09-30"
        )
        sleepDao.insert(entity)
        entity = SleepData(
            start_time = "2021-07-20-00-21",
            end_time = "2021-07-20-09-00"
        )
        sleepDao.insert(entity)
        entity = SleepData(
            start_time = "2021-07-20-23-21",
            end_time = "2021-07-21-08-30"
        )
        sleepDao.insert(entity)
        entity = SleepData(
            start_time = "2021-07-22-01-21",
            end_time = "2021-07-22-09-30"
        )
        sleepDao.insert(entity)
        entity = SleepData(
            start_time = "2021-07-23-01-21",
            end_time = "2021-07-23-09-30"
        )
        sleepDao.insert(entity)
    }

}
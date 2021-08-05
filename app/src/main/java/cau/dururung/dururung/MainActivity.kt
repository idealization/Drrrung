package cau.dururung.dururung

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
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
import java.text.SimpleDateFormat
import java.util.*



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sleepDao: SleepDataDao
    private lateinit var labels: Array<String>

    var hour: Int = 0
    var min: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        accessDatabase()
        // 임의로 데이터 삽입
        Thread(insertSleep).start()

        val sleepdata = sleepDao.getWeek()
        val entries = ArrayList<CandleEntry>()
        var i = 0f
        labels = arrayOf("0","0","0","0","0","0","0")
        for (data in sleepdata) {
            val sleepdate_tmp = data.sleep_date.split('-')
            labels.set(i.toInt(),sleepdate_tmp[1]+"/"+sleepdate_tmp[2])
            val start_tmp = data.start_time.split('-')
            val start_time = start_tmp[0].toFloat()
            val start_min = start_tmp[1].toFloat()
            val new_smin = start_min * 10 / 6
            val end_tmp = data.end_time.split('-')
            val end_time = end_tmp[0].toFloat()
            val end_min = end_tmp[1].toFloat()
            val new_emin = end_min * 10 / 6
            var new_stime = start_time - 12
            if (new_stime < 0) {
                new_stime += 24
            }
            entries.add(CandleEntry(i,0f,0f,end_time + new_emin/100,new_stime + new_smin/100))
            i += 1
        }

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
            axisLeft.axisMaximum = 24f
            axisRight.axisMaximum = 24f
            animateXY(0,800)

            axisRight.setDrawLabels(false)
            xAxis.setDrawGridLines(false)

            setScaleEnabled(false)
            setPinchZoom(false)


            description.isEnabled = false
            isHighlightPerDragEnabled = true
            requestDisallowInterceptTouchEvent(true)

            //val labels = arrayOf("월", "화", "수","목","금","토","일")
            this.xAxis.valueFormatter = object: ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return labels[value.toInt()]
                }
            }
            invalidate()
        }

        binding.btnSelectTime.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                binding.btnSelectTime.text = SimpleDateFormat("HH:mm").format(cal.time)
            }

            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        binding.switchWakeTime.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.btnSelectTime.isEnabled = isChecked
        }

    }

    private fun accessDatabase() {
        val db = SleepDatabase.getInstance(this)!!
        sleepDao = db.sleepDao()
    }

    private val insertSleep = Runnable {

        var entity = SleepData(
            sleep_date = "2021-07-19",
            start_time = "01-21",
            wakeUp_date = "2021-07-19",
            end_time = "09-30",
            rating = 3.0f
        )
        sleepDao.insert(entity)
        entity = SleepData(
            sleep_date = "2021-07-20",
            start_time = "00-21",
            wakeUp_date = "2021-07-20",
            end_time = "09-00",
            rating = 3.0f
        )
        sleepDao.insert(entity)
        entity = SleepData(
            sleep_date = "2021-07-20",
            start_time = "23-21",
            wakeUp_date = "2021-07-21",
            end_time = "08-30",
            rating = 3.0f
        )
        sleepDao.insert(entity)
        entity = SleepData(
            sleep_date = "2021-07-22",
            start_time = "01-21",
            wakeUp_date = "2021-07-22",
            end_time = "09-30",
            rating = 3.0f
        )
        sleepDao.insert(entity)
        entity = SleepData(
            sleep_date = "2021-07-23",
            start_time = "01-21",
            wakeUp_date = "2021-07-23",
            end_time = "09-30",
            rating = 3.0f
        )
        sleepDao.insert(entity)
        entity = SleepData(
            sleep_date = "2021-07-24",
            start_time = "01-21",
            wakeUp_date = "2021-07-24",
            end_time = "09-30",
            rating = 3.0f
        )
        sleepDao.insert(entity)
        entity = SleepData(
            sleep_date = "2021-07-25",
            start_time = "01-21",
            wakeUp_date = "2021-07-25",
            end_time = "09-30",
            rating = 3.0f
        )
        sleepDao.insert(entity)
    }

}
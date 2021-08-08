package cau.dururung.dururung

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cau.dururung.dururung.databinding.ActivityMainBinding
import cau.dururung.dururung.db.SleepData
import cau.dururung.dururung.db.SleepDataDao
import cau.dururung.dururung.db.SleepDatabase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sleepDao: SleepDataDao
    private lateinit var labels: Array<String>
    lateinit var audioTrack: AudioTrack
    lateinit var equalizer: Equalizer

    var ison = false
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

        binding.whiteNoiseActivity.setOnClickListener {
            val nextIntent = Intent(this, WhiteNoiseActivity::class.java)
            startActivity(nextIntent)
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

        binding.morningCallActivity.setOnClickListener {
            val nextIntent = Intent(this, MorningcallActivity::class.java)
            startActivity(nextIntent)
        }

        binding.playWhiteSound.setOnClickListener {
            if (ison) {
                audioTrack.stop()
                ison = false
                return@setOnClickListener
            }

            val duration = 10 // duration of sound
            val sampleRate = 44100 // Hz (maximum frequency is 7902.13Hz (B8))
            val numSamples = duration * sampleRate
            val samples = DoubleArray(numSamples)
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                buffer[i] = (Short.MIN_VALUE..Short.MAX_VALUE).random().toShort()
            }

            audioTrack = AudioTrack.Builder()
                .setAudioFormat(
                    AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(44100)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build())
                .setTransferMode(AudioTrack.MODE_STATIC)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build())
                .setBufferSizeInBytes(buffer.size * 2)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            equalizer = Equalizer(0, audioTrack.audioSessionId)

            val file_wn = File(applicationContext.getExternalFilesDir(null), "white_noise")
            if (!file_wn.exists()) return@setOnClickListener
            val textFormat = file_wn.readText()
            val setlist = textFormat.split(" ")

            for (i in 0 until equalizer.numberOfBands){
//            Log.d("WNoise", "${equalizer.getBandFreqRange(i.toShort())[1]}")
                Log.d("WNoise", "$i gain: ${equalizer.getBandLevel(i.toShort())}")
                equalizer.setBandLevel(i.toShort(), (setlist[i].toFloat() * 3000 - 1500).toInt().toShort())
            }
            equalizer.enabled = true
            audioTrack.setLoopPoints(0, buffer.size, -1);
            audioTrack.play()
            ison = true
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
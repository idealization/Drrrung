package cau.dururung.dururung


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.time.LocalDate
import java.util.*

class PassActivity() : AppCompatActivity() {
    var timer = Timer()
    val cal = Calendar.getInstance()
    private var hour = 0
    private var min = 0
    private lateinit var selectedSoundName : String
    private var volume = 10
    private var snoozeCnt = 0

    private var sleepDate = "NaN"
    private var sleepTime = "NaN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)

        sleepDate = intent.getStringExtra("sleepDate").toString()
        sleepTime = intent.getStringExtra("sleepTime").toString()

        Log.d("start_slp",sleepDate+" " +sleepTime)

        val appSpecificExternalDir =
            File(applicationContext.getExternalFilesDir(null), "alarm_info")

        if (appSpecificExternalDir.canRead()){
            val alarmStr = appSpecificExternalDir.readText()
            val alarmData = alarmStr.split(' ')
            // "$hour $min $selectedSoundName $volume"
            hour = alarmData[0].toInt()
            min = alarmData[1].toInt()
            selectedSoundName = alarmData[2]
            volume = alarmData[3].toInt()




            cal.set(Calendar.HOUR, hour)
            cal.set(Calendar.MINUTE, min)

            Log.d("time",alarmStr)

            timer.schedule(object : TimerTask() {
                override fun run() {
                    val intent = Intent(this@PassActivity, AlarmActivity::class.java)
                    val ringData = arrayOf(selectedSoundName,volume)
                    intent.putExtra("ring",ringData)
                    startActivityForResult(intent,3000)
                }
            }, cal.time)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when (requestCode){
                3000 -> {
                    val isSnooze = data!!.getStringExtra("isSnooze").toBoolean()
                    if (!isSnooze){
                        val today = LocalDate.now().toString()
                        var hourStr = hour.toString()
                        var minStr = min.toString()
                        if (hour < 10){ hourStr = "0" + hourStr}
                        if (min < 10){ minStr = "0" + minStr}
                        val endTime = hourStr + ":" + minStr


                        val intent = Intent(this@PassActivity, RatingActivity::class.java)
                        val data = arrayOf(sleepDate, sleepTime, today, endTime)
                        intent.putExtra("sleepData",data)
                        startActivity(intent)
                        timer.cancel()
                        finish()

                    }
                    else if(isSnooze){
                        snoozeCnt += 1
                        min += 5
                        if (min >= 60){
                            hour = (hour + 1) % 24
                            min -= 60
                        }

                        cal.set(Calendar.HOUR, hour)
                        cal.set(Calendar.MINUTE, min)

                        timer.cancel()
                        timer = Timer()
                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                val intent = Intent(this@PassActivity, AlarmActivity::class.java)
                                val ringData = arrayOf(selectedSoundName,volume)
                                intent.putExtra("ring",ringData)
                                startActivityForResult(intent,3000)
                            }
                        }, cal.time)
                    }
                }
            }
        }
    }
}
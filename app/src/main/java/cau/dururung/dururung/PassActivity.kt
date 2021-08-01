package cau.dururung.dururung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.io.File
import java.util.*

class PassActivity : AppCompatActivity() {
    var timer = Timer()
    val cal = Calendar.getInstance()
    private var hour = 0
    private var min = 0
    private lateinit var selectedSoundName : String
    private var volume = 10
    private var snoozeCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)

        val appSpecificExternalDir =
            File(applicationContext.getExternalFilesDir(null), "alarm_info")

        if (appSpecificExternalDir.canRead()){
            val alarmStr = appSpecificExternalDir.readText()
            var alarmData = alarmStr.split(' ')
            // "$hour $min $selectedSoundName $volume"
            hour = alarmData[0].toInt()
            min = alarmData[1].toInt()
            selectedSoundName = alarmData[2]
            volume = alarmData[3].toInt()

            cal.set(Calendar.HOUR,hour)
            cal.set(Calendar.MINUTE, min)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when (requestCode){
                3000 -> {
                    val isSnooze = data!!.getStringExtra("isSnooze").toBoolean()
                    if (!isSnooze){
                        //데이터저장
                        timer.cancel()
                        finish()
                    }
                    else if(isSnooze){
                        snoozeCnt += 1
                        min += 1
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
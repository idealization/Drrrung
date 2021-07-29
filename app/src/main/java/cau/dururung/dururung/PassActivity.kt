package cau.dururung.dururung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.io.File
import java.util.*

class PassActivity : AppCompatActivity() {
    val timer = Timer()
    val cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)

        val appSpecificExternalDir =
            File(applicationContext.getExternalFilesDir(null), "alarm_info")


        if (appSpecificExternalDir.canRead()){
            val alarmStr = appSpecificExternalDir.readText()
            var alarmData = alarmStr.split(' ')
            // "$hour $min $selectedSoundName $volume"
            var hour = alarmData.get(0).toInt()
            var min = alarmData.get(1).toInt()
            val selectedSoundName = alarmData.get(2)
            val volume = alarmData.get(3).toInt()

            var t = Toast.makeText(this,"$hour $min $selectedSoundName $volume",Toast.LENGTH_LONG)
            t.show()

            cal.set(Calendar.HOUR,hour)
            cal.set(Calendar.MINUTE, min)

            timer.schedule(object : TimerTask() {
                override fun run() {
                    val intent = Intent(this@PassActivity, AlarmActivity::class.java)
                    val ringData = arrayOf(selectedSoundName,volume)
                    intent.putExtra("ring",ringData)
                    setResult(RESULT_OK, intent)
                    startActivity(intent)
                    //finish()
                }
            }, cal.time)

            if (intent.getStringExtra("isSnooze").equals("false")){
                //데이터저장
                timer.cancel()
                val t = Toast.makeText(this,"취소",Toast.LENGTH_LONG)
                t.show()
                finish()
            }
            else if(intent.getStringExtra("isSnooze").equals("true")){
                val t = Toast.makeText(this,"취소2",Toast.LENGTH_LONG)
                t.show()
                min += 5
                if (min >= 60){
                    hour = (hour + 1) % 24
                    min -= 60
                }

                var t = Toast.makeText(this,"$hour $min $selectedSoundName $volume",Toast.LENGTH_LONG)
                t.show()
                cal.set(Calendar.HOUR,hour)
                cal.set(Calendar.MINUTE, min)

                //timer.cancel()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        val intent = Intent(this@PassActivity, AlarmActivity::class.java)
                        val ringData = arrayOf(selectedSoundName,volume)
                        intent.putExtra("ring",ringData)
                        setResult(RESULT_OK, intent)
                        startActivity(intent)
                        //finish()
                    }
                }, cal.time)
            }

        }



    }
}
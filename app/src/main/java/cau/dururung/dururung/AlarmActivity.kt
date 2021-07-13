package cau.dururung.dururung


import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import cau.dururung.dururung.databinding.ActivityAlarmBinding
import cau.dururung.dururung.databinding.ActivityRatingBinding
import java.lang.Exception

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var r2: Ringtone
    private lateinit var vib:Vibrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
    try {
        vib = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vib.vibrate(1000)
        }

        //val alarmName = "ringstone"
        //val alarm: Uri = Uri.parse("android.resource://"+packageName+"/raw/ringstone.mp3")
        val alarm: Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.ringstone)
        //val alarm: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r2 = RingtoneManager.getRingtone(this, alarm)
        r2.play()
    } catch (e:Exception){
        e.printStackTrace()
    }
        binding.stopBtn.setOnClickListener {
            r2.stop()
            vib.cancel()
        }

    }


}
package cau.dururung.dururung

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import cau.dururung.dururung.databinding.ActivityAlarmBinding
import java.util.*


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
        //val alarm: Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.ringstone)
        val alarm: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r2 = RingtoneManager.getRingtone(this, alarm)
        /*
        val mMediaPlayer = MediaPlayer()
        try {
            mMediaPlayer.setDataSource(this, alarm)
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
            mMediaPlayer.isLooping = true
            mMediaPlayer.prepare()
            mMediaPlayer.start()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        */
        r2.play()
    } catch (e:Exception){
        e.printStackTrace()
    }
        var intent = Intent(this@AlarmActivity, RatingActivity::class.java)
        binding.stopBtn.setOnClickListener {
            r2.stop()
            vib.cancel()
            intent.putExtra("isSnooze",false)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.snooze.setOnClickListener {
            r2.stop()
            vib.cancel()
            intent.putExtra("isSnooze",true)
            setResult(RESULT_OK, intent)
            finish()
        }


    }


}
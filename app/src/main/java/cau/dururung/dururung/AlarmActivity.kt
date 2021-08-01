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
import java.lang.reflect.Array
import java.util.*


class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private lateinit var r2: Ringtone
    private lateinit var vib:Vibrator
    private lateinit var player: MediaPlayer

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

        val ringData = intent.getStringExtra("ring")
        val alarmName = ringData?.get(0)
        player = MediaPlayer.create(this, R.raw.illuminate)
        //var alarm: Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.illuminate)
        if (alarmName != null) {
            if (alarmName.equals("cosmic")){
                player = MediaPlayer.create(this, R.raw.cosmic)
                //alarm = Uri.parse("android.resource://"+packageName+"/"+R.raw.cosmic)
            }
            else if (alarmName.equals("crystals")){
                player = MediaPlayer.create(this, R.raw.crystals)
                //alarm = Uri.parse("android.resource://"+packageName+"/"+R.raw.crystals)
            }
            else if (alarmName.equals("hillside")){
                player = MediaPlayer.create(this, R.raw.hillside)
                //alarm = Uri.parse("android.resource://"+packageName+"/"+R.raw.hillside)
            }
        }
        //r2 = RingtoneManager.getRingtone(this, alarm)

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

        r2.play()
        */
        player.isLooping = true
        player.start()
    } catch (e:Exception){
        e.printStackTrace()
    }
        val resultIntent = Intent(this,PassActivity::class.java)
        binding.stopBtn.setOnClickListener {
            //r2.stop()
            player.stop()
            vib.cancel()
            resultIntent.putExtra("isSnooze","false")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        binding.snooze.setOnClickListener {
            //r2.stop()
            player.stop()
            vib.cancel()
            resultIntent.putExtra("isSnooze","true")
            setResult(RESULT_OK, resultIntent)
            finish()
        }


    }


}
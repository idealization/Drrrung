package cau.dururung.dururung

import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.SeekBar
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import cau.dururung.dururung.databinding.ActivityMorningcallBinding
import java.io.File
import java.util.*

class MorningcallActivity : AppCompatActivity() {

    lateinit var binding: ActivityMorningcallBinding
    val positiveButtonClick = { dialogInterface: DialogInterface, i: Int ->
        toast("Positive")
    }
    val negativeButtonClick = { dialogInterface: DialogInterface, i: Int ->
        toast("Negative")
    }
    val neutralButtonClick = { dialogInterface: DialogInterface, i: Int ->
        toast("Neutral")
    }

    var hour: Int = 0
    var min: Int = 0
    lateinit var audioManager: AudioManager
    lateinit var selectedSound: MediaPlayer
    lateinit var selectedSoundName: String
    var volume: Int = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morningcall)


        val appSpecificExternalDir =
            File(applicationContext.getExternalFilesDir(null), "alarm_info")

        binding = ActivityMorningcallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timePicker.setOnTimeChangedListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
            hour = hourOfDay
            min = minute
        }

        val musics = arrayOf(
            MediaPlayer.create(this, R.raw.cosmic),
            MediaPlayer.create(this, R.raw.crystals),
            MediaPlayer.create(this, R.raw.hillside),
            MediaPlayer.create(this, R.raw.illuminate)
        )
        selectedSound = musics[0]
        binding.btnRadioAlertDialog.setOnClickListener {
            val items = arrayOf("Cosmic", "Crystals", "Hillside", "Illuminate")
            lateinit var selectedItem: String
            val builder = AlertDialog.Builder(this)
                .setTitle("Select Item")
                .setSingleChoiceItems(items, -1) { dialog, which ->
                    selectedItem = items[which]
                    musics[items.indexOf(selectedItem)].start()
                }
                .setPositiveButton("OK") { dialog, which ->
                    toast("$selectedItem is Selected")
                    selectedSound = musics[items.indexOf(selectedItem)]
                    selectedSoundName = selectedItem
                    musics[items.indexOf(selectedItem)].pause()
                }
                .show()
        }

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val seekBarListener = SeekBarListener()
        binding.seekBar.setOnSeekBarChangeListener(seekBarListener)

        binding.okBtn.setOnClickListener {
            // 보림
            var intent = Intent(this@MorningcallActivity, PassActivity::class.java)
            appSpecificExternalDir.writeText("$hour $min $selectedSoundName $volume")
            startActivity(intent)
        }

        binding.cancleBtn.setOnClickListener {
            // 보림
            var intent = Intent(this@MorningcallActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inner class SeekBarListener : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            Log.d(
                TAG,
                String.format(
                    "onProgressChanged 값 변경 중 : progress [%d] fromUser [%b]",
                    progress,
                    fromUser
                )
            )
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                progress,
                AudioManager.FLAG_SHOW_UI
            )
            volume = progress
            selectedSound.isLooping
            selectedSound.start()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }
}
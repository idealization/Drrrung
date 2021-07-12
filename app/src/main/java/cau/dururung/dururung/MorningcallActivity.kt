package cau.dururung.dururung

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import cau.dururung.dururung.databinding.ActivityMorningcallBinding

class MorningcallActivity : AppCompatActivity() {

    lateinit var binding : ActivityMorningcallBinding
    val positiveButtonClick = { dialogInterface: DialogInterface, i: Int ->
        toast("Positive")
    }
    val negativeButtonClick = { dialogInterface: DialogInterface, i: Int ->
        toast("Negative")
    }
    val neutralButtonClick = { dialogInterface: DialogInterface, i: Int ->
        toast("Neutral")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morningcall)

        binding = ActivityMorningcallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRadioAlertDialog.setOnClickListener {
            val items = arrayOf("Apple", "Orange", "Mango", "Lemon")
            var selectedItem: String? = null
            val builder = AlertDialog.Builder(this)
                .setTitle("Select Item")
                .setSingleChoiceItems(items, -1) { dialog, which ->
                    selectedItem = items[which]
                }
                .setPositiveButton("OK") { dialog, which ->
                    toast("${selectedItem.toString()} is Selected")
                }
                .show()
        }

        var seekBarListener = SeekBarListener()
        binding.seekBar.setOnSeekBarChangeListener(seekBarListener)
    }

    fun toast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inner class SeekBarListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            //TODO("Not yet implemented")
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            //TODO("Not yet implemented")
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //TODO("Not yet implemented")
        }
    }
}
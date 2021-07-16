package cau.dururung.dururung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.File

class WhiteNoiseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_white_noise)

        val file = File(applicationContext.getExternalFilesDir(null)!!.absolutePath + "/alarm")
        file.writeText("Hello world!")
        Log.d("WNoise", "${file}")
    }
}


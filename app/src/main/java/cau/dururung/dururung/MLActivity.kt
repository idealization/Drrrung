package cau.dururung.dururung

import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import cau.dururung.dururung.databinding.ActivityMlactivityBinding
import cau.dururung.dururung.databinding.ActivityMorningcallBinding
import cau.dururung.dururung.databinding.ActivityRatingBinding
import cau.dururung.dururung.ml.ConvertedModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class MLActivity() : AppCompatActivity() {

    companion object {
        lateinit var instance: MLActivity
        private set
    }
    val context: Context
        get() = applicationContext
    
    private lateinit var binding: ActivityMlactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MLActivity.instance = this

        binding = ActivityMlactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var hour = 0
        var min = 0
        binding.timePicker.setOnTimeChangedListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
            hour = hourOfDay
            min = minute
        }
        var startTime = hour + min.toFloat() / 60

        // ML 돌리기
        val model = ConvertedModel.newInstance(context)

// Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 2), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocate(16)
            .putFloat(startTime)
            .putFloat(5f)
        inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
        val output1 = model.process(inputFeature0)
        val outputFeature0 = output1.outputFeature0AsTensorBuffer
        val outputVal = outputFeature0.getFloatValue(0)
        binding.predicted.setText(outputVal.toString())


// Releases model resources if no longer used.
        model.close()


//        Interpreter(converted_model.tflite").use { interpreter ->
//            val inputs: MutableMap<String, Any> = HashMap()
//            inputs["Slept"] = input1
//            inputs["Rating"] = input2
//            val outputs: MutableMap<String, Any> = HashMap()
//            outputs["output_1"] = output1
//            interpreter.runSignature(inputs, outputs, "mySignature")
//        }
    }
}


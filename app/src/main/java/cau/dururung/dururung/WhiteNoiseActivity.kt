package cau.dururung.dururung

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class WhiteNoiseActivity : AppCompatActivity() {
    lateinit var audioTrack: AudioTrack
    lateinit var equalizer: Equalizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_white_noise)

        val duration = 10 // duration of sound
        val sampleRate = 44100 // Hz (maximum frequency is 7902.13Hz (B8))
        val numSamples = duration * sampleRate
        val samples = DoubleArray(numSamples)
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            buffer[i] = (Short.MIN_VALUE..Short.MAX_VALUE).random().toShort()
        }

        audioTrack = AudioTrack.Builder()
            .setAudioFormat(AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build())
            .setTransferMode(AudioTrack.MODE_STREAM)
            .setAudioAttributes(AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build())
            .setBufferSizeInBytes(buffer.size * 2)
            .build()

        audioTrack.write(buffer, 0, buffer.size)

        equalizer = Equalizer(0, audioTrack.audioSessionId)
        for (i in 0 until equalizer.numberOfBands){
            Log.d("WNoise", "${equalizer.getBandFreqRange(i.toShort())[1]}")
        }

        audioTrack.play()


    }
}


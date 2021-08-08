package cau.dururung.dururung

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cau.dururung.dururung.databinding.ActivityRatingBinding


class RatingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    var ratingValue: Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingValue = rating
        }
        binding.save.setOnClickListener {
            Toast.makeText(this, "${ratingValue}점", Toast.LENGTH_SHORT).show()
        }
    }
}

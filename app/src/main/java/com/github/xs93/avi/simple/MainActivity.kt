package com.github.xs93.avi.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.xs93.avi.simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            smoothToShow.setOnClickListener {
                loadingView.smoothToShow()
            }

            smoothToHide.setOnClickListener {
                loadingView.smoothToHide()
            }

            show.setOnClickListener {
                loadingView.show()
            }

            hide.setOnClickListener {
                loadingView.hide()
            }

            startAnimation.setOnClickListener {
                loadingView.startAnimation()
            }

            stopAnimation.setOnClickListener {
                loadingView.stopAnimation()
            }
        }

    }
}
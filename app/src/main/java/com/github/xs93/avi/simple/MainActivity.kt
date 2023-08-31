package com.github.xs93.avi.simple

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.xs93.avi.indicator.BallClipRotateMultipleIndicator
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

            changeLoading.setOnClickListener {
                val loading = BallClipRotateMultipleIndicator()
                loadingView.setIndicator(loading)
            }
        }

        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f).apply {
            duration = 600
            repeatCount = -1
            addUpdateListener {

            }
        }
        rotateAnim.start()
    }
}
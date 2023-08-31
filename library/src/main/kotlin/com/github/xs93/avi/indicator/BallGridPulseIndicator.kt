package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator
import kotlin.math.min

/**
 * 9宫格点阵，效果查看第1行第2个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 9:58
 * @email 466911254@qq.com
 */
open class BallGridPulseIndicator : Indicator() {

    companion object {

        private const val ALPHA = 255
        private const val SCALE = 1.0f
    }

    protected val alphas = IntArray(9) { ALPHA }
    protected val scales = FloatArray(9) { SCALE }

    override fun draw(canvas: Canvas, paint: Paint) {
        val circleSpacing = 4f
        val radius = (min(width, height) - circleSpacing * 4) / 6
        val x = width / 2f - (radius * 2 + circleSpacing)
        val y = height / 2f - (radius * 2 + circleSpacing)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                canvas.save()
                val translationX = x + (radius * 2 + circleSpacing) * j
                val translationY = y + (radius * 2 + circleSpacing) * i
                canvas.translate(translationX, translationY)
                canvas.scale(scales[3 * i + j], scales[3 * i + j])
                paint.alpha = alphas[3 * i + j]
                canvas.drawCircle(0f, 0f, radius, paint)
                canvas.restore()
            }
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val durations = longArrayOf(720, 1020, 1280, 1420, 1450, 1180, 870, 1450, 1060)
        val delays = longArrayOf(-60, 250, -170, 480, 310, 30, 460, 780, 450)
        for (i in 0 until 9) {
            val scaleAnimator = ValueAnimator.ofFloat(1f, 0.5f, 1f).apply {
                duration = durations[i]
                repeatCount = -1
                startDelay = delays[i]
                interpolator = LinearInterpolator()
            }
            addUpdateListener(scaleAnimator) {
                scales[i] = it.animatedValue as Float
                postInvalidate()
            }

            val alphaAnimator = ValueAnimator.ofInt(255, 210, 122, 255).apply {
                duration = durations[i]
                repeatCount = -1
                startDelay = delays[i]
                interpolator = LinearInterpolator()
            }
            addUpdateListener(alphaAnimator) {
                alphas[i] = it.animatedValue as Int
                postInvalidate()
            }

            animators.add(scaleAnimator)
            animators.add(alphaAnimator)
        }
        return animators
    }
}
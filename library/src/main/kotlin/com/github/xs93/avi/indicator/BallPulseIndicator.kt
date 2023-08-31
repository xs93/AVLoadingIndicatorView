package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator
import kotlin.math.min

/**
 * 三球循环动画指示器，效果查看第1行第1个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/30 14:40
 * @email 466911254@qq.com
 */
class BallPulseIndicator : Indicator() {

    companion object {

        private const val SCALE = 1.0f
    }

    private val scaleFloats = floatArrayOf(SCALE, SCALE, SCALE)

    override fun draw(canvas: Canvas, paint: Paint) {
        val circleSpacing = 4f
        val radius = (min(width, height) - circleSpacing * 2) / 6
        val x = width / 2f - (radius * 2 + circleSpacing)
        val y = height / 2f
        for (index in 0 until 3) {
            canvas.save()
            val translateX = x + (radius * 2) * index + circleSpacing * index
            canvas.translate(translateX, y)
            canvas.scale(scaleFloats[index], scaleFloats[index])
            canvas.drawCircle(0f, 0f, radius, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = arrayListOf<ValueAnimator>()
        val delays = longArrayOf(120, 240, 360)
        for (index in 0 until 3) {
            val scaleAnimator = ValueAnimator.ofFloat(1f, 0.3f, 1f).apply {
                duration = 720
                repeatCount = -1
                interpolator = LinearInterpolator()
                startDelay = delays[index]
            }
            addUpdateListener(scaleAnimator) {
                scaleFloats[index] = it.animatedValue as Float
                postInvalidate()
            }

            animators.add(scaleAnimator)
        }
        return animators
    }
}
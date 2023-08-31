package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 * 效果查看 4-4
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:16
 * @email 466911254@qq.com
 */
open class BallScaleMultipleIndicator : Indicator() {

    protected val scaleFloats = floatArrayOf(1f, 1f, 1f)
    protected val alphaInts = intArrayOf(255, 255, 255)
    private val circleSpacing = 4f
    override fun draw(canvas: Canvas, paint: Paint) {
        for (i in 0..2) {
            paint.alpha = alphaInts[i]
            canvas.scale(scaleFloats[i], scaleFloats[i], width / 2f, height / 2f)
            canvas.drawCircle(width / 2f, height / 2f, width / 2f - circleSpacing, paint)
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val delays = longArrayOf(0, 200, 400)
        for (i in 0..2) {
            val scaleAnim = ValueAnimator.ofFloat(0f, 1f).apply {
                interpolator = LinearInterpolator()
                duration = 1000
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }

            val alphaAnim = ValueAnimator.ofInt(255, 0).apply {
                interpolator = LinearInterpolator()
                duration = 1000
                repeatCount = -1
                startDelay = delays[i]
            }

            addUpdateListener(alphaAnim) { animation ->
                alphaInts[i] = animation.animatedValue as Int
                postInvalidate()
            }

            animators.add(scaleAnim)
            animators.add(alphaAnim)
        }
        return animators
    }
}
package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import com.github.xs93.avi.Indicator


/**
 *
 * 效果查看 5-2
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:29
 * @email 466911254@qq.com
 */
class BallBeatIndicator : Indicator() {

    companion object {

        private const val ALPHA = 255
        private const val SCALE = 1.0f
    }

    private val scales = FloatArray(3) { SCALE }
    private val alphas = IntArray(3) { ALPHA }
    private val circleSpacing = 4f
    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = (width - circleSpacing * 2) / 6
        val x = width / 2 - (radius * 2 + circleSpacing)
        val y = height / 2f
        for (i in 0..2) {
            canvas.save()
            val translateX = x + radius * 2 * i + circleSpacing * i
            canvas.translate(translateX, y)
            canvas.scale(scales[i], scales[i])
            paint.alpha = alphas[i]
            canvas.drawCircle(0f, 0f, radius, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val delays = longArrayOf(350, 0, 350)
        for (i in 0..2) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.75f, 1f).apply {
                duration = 700
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                scales[i] = animation.animatedValue as Float
                postInvalidate()
            }

            val alphaAnim = ValueAnimator.ofInt(255, 51, 255).apply {
                duration = 700
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(alphaAnim) { animation ->
                alphas[i] = animation.animatedValue as Int
                postInvalidate()
            }
            animators.add(scaleAnim)
            animators.add(alphaAnim)
        }
        return animators
    }
}
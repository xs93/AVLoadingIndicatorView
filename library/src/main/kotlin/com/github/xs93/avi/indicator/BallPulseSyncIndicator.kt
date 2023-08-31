package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import com.github.xs93.avi.Indicator


/**
 *
 * 效果查看 5-1
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:24
 * @email 466911254@qq.com
 */
class BallPulseSyncIndicator : Indicator() {

    private val translateYFloats = FloatArray(3)
    private val circleSpacing = 4f

    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = (width - circleSpacing * 2) / 6
        val x = width / 2 - (radius * 2 + circleSpacing)
        for (i in 0..2) {
            canvas.save()
            val translateX = x + radius * 2 * i + circleSpacing * i
            canvas.translate(translateX, translateYFloats[i])
            canvas.drawCircle(0f, 0f, radius, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val radius = (width - circleSpacing * 2) / 6
        val delays = longArrayOf(70, 140, 210)
        for (i in 0..2) {
            val scaleAnim = ValueAnimator.ofFloat(height / 2f, height / 2 - radius * 2, height / 2f).apply {
                duration = 600
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                translateYFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            animators.add(scaleAnim)
        }
        return animators
    }
}
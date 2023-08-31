package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.github.xs93.avi.Indicator


/**
 * 效果查看 4-3
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:13
 * @email 466911254@qq.com
 */
class LineScalePartyIndicator : Indicator() {

    companion object {

        private const val SCALE = 1f
    }

    private val scaleFloats = FloatArray(5) { SCALE }
    private val rectF = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {
        val translateX = width / 9f
        val translateY = height / 2f
        for (i in 0..3) {
            canvas.save()
            canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            rectF.set(-translateX / 2, -height / 2.5f, translateX / 2, height / 2.5f)
            canvas.drawRoundRect(rectF, translateX, translateX, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val durations = longArrayOf(1260, 430, 1010, 730)
        val delays = longArrayOf(770, 290, 280, 740)
        for (i in 0..3) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.4f, 1f).apply {
                repeatCount = -1
                duration = durations[i]
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            animators.add(scaleAnim)
        }
        return animators
    }
}
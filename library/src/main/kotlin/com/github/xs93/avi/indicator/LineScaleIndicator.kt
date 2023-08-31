package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.github.xs93.avi.Indicator


/**
 * 效果查看 4-2
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:06
 * @email 466911254@qq.com
 */
open class LineScaleIndicator : Indicator() {

    companion object {

        private const val SCALE = 1f
    }

    protected val scaleYFloats = FloatArray(5) { SCALE }
    private val rectF = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {
        val translateX = (width / 11).toFloat()
        val translateY = (height / 2).toFloat()
        for (i in 0..4) {
            canvas.save()
            canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY)
            canvas.scale(SCALE, scaleYFloats[i])
            rectF.set(-translateX / 2, -height / 2.5f, translateX / 2, height / 2.5f)
            canvas.drawRoundRect(rectF, translateX, translateX, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val delays = longArrayOf(100, 200, 300, 400, 500)
        for (i in 0..4) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.4f, 1f).apply {
                duration = 1000
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                scaleYFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            animators.add(scaleAnim)
        }
        return animators
    }
}
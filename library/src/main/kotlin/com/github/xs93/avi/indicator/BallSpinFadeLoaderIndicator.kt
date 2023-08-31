package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import com.github.xs93.avi.Indicator
import kotlin.math.cos
import kotlin.math.sin


/**
 * 效果查看 6-3
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:43
 * @email 466911254@qq.com
 */
open class BallSpinFadeLoaderIndicator : Indicator() {

    companion object {

        private const val ALPHA = 255
        private const val SCALE = 1.0f
    }

    protected val scaleFloats = FloatArray(9) { SCALE }
    protected val alphas = IntArray(9) { ALPHA }
    protected val point: PointF = PointF(0f, 0f)

    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = (width / 10).toFloat()
        for (i in 0..7) {
            canvas.save()
            circleAt(width, height, width / 2 - radius, i * (Math.PI / 4), point)
            canvas.translate(point.x, point.y)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            paint.alpha = alphas[i]
            canvas.drawCircle(0f, 0f, radius, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val delays = longArrayOf(0, 120, 240, 360, 480, 600, 720, 780, 840)
        for (i in 0..7) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.4f, 1f).apply {
                duration = 1000
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            val alphaAnim = ValueAnimator.ofInt(255, 77, 255).apply {
                duration = 1000
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

    protected fun circleAt(width: Int, height: Int, radius: Float, angle: Double, pointF: PointF) {
        val x = (width / 2f + radius * cos(angle)).toFloat()
        val y = (height / 2f + radius * sin(angle)).toFloat()
        pointF.set(x, y)
    }

}
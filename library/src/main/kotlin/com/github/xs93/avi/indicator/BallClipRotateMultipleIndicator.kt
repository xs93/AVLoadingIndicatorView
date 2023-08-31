package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.github.xs93.avi.Indicator


/**
 *效果查看第2行第2个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 10:47
 * @email 466911254@qq.com
 */
class BallClipRotateMultipleIndicator : Indicator() {

    private val circleSpacing = 12f
    private var scaleFloat: Float = 1f
    private var degrees: Float = 0f

    private val bStartAngles = floatArrayOf(135f, -45f)
    private val sStartAngles = floatArrayOf(225f, 45f)

    private val rectF = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f

        val x = width / 2f
        val y = height / 2f

        canvas.save()
        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.rotate(degrees)

        rectF.set(-x + circleSpacing, -y + circleSpacing, x - circleSpacing, y - circleSpacing)
        for (i in 0 until 2) {
            canvas.drawArc(rectF, bStartAngles[i], 90f, false, paint)
        }
        canvas.restore()

        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.rotate(-degrees)
        rectF.set(
            -x / 1.8f + circleSpacing,
            -y / 1.8f + circleSpacing,
            x / 1.8f - circleSpacing,
            y / 1.8f - circleSpacing
        )
        for (i in 0 until 2) {
            canvas.drawArc(rectF, sStartAngles[i], 90f, false, paint)
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val scaleAnim = ValueAnimator.ofFloat(1f, 0.6f, 1f).apply {
            duration = 1000
            repeatCount = -1
        }
        addUpdateListener(scaleAnim) { animation ->
            scaleFloat = animation.animatedValue as Float
            postInvalidate()
        }

        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f).apply {
            duration = 1000
            repeatCount = -1
        }
        addUpdateListener(rotateAnim) { animation ->
            degrees = animation.animatedValue as Float
            postInvalidate()
        }
        animators.add(scaleAnim)
        animators.add(rotateAnim)
        return animators
    }
}
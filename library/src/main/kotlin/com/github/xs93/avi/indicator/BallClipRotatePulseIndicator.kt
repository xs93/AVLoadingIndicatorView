package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.github.xs93.avi.Indicator


/**
 * 效果查看第一行第4个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 10:26
 * @email 466911254@qq.com
 */
class BallClipRotatePulseIndicator : Indicator() {

    private var scaleFloat: Float = 1f
    private var scaleFloat2: Float = 1f
    private var degrees: Float = 0f
    private val circleSpacing = 12f

    private val startAngles = floatArrayOf(225f, 45f)
    private val rectF = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {

        val x = width / 2f
        val y = width / 2f

        canvas.save()
        paint.style = Paint.Style.FILL
        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.drawCircle(0f, 0f, x / 2.5f, paint)
        canvas.restore()


        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f

        canvas.translate(x, y)
        canvas.scale(scaleFloat2, scaleFloat2)
        canvas.rotate(degrees)
        for (i in 0 until 2) {
            rectF.set(-x + circleSpacing, -y + circleSpacing, x - circleSpacing, y - circleSpacing)
            canvas.drawArc(rectF, startAngles[i], 90f, false, paint)
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val scaleAnim = ValueAnimator.ofFloat(1f, 0.3f, 1f).apply {
            duration = 1000
            repeatCount = -1
        }
        addUpdateListener(scaleAnim) { animation ->
            scaleFloat = animation.animatedValue as Float
            postInvalidate()
        }

        val scaleAnim2 = ValueAnimator.ofFloat(1f, 0.6f, 1f).apply {
            duration = 1000
            repeatCount = -1
        }
        addUpdateListener(scaleAnim2) { animation ->
            scaleFloat2 = animation.animatedValue as Float
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

        val animators = ArrayList<ValueAnimator>()
        animators.add(scaleAnim)
        animators.add(scaleAnim2)
        animators.add(rotateAnim)
        return animators
    }
}
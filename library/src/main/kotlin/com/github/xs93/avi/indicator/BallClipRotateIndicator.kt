package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.github.xs93.avi.Indicator


/**
 * 效果查看第1行第3个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 10:18
 * @email 466911254@qq.com
 */
class BallClipRotateIndicator : Indicator() {

    private var scaleFloat: Float = 1f
    private var degrees: Float = 0f
    private val circleSpacing = 12f

    private val rectF = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        val x = width / 2f
        val y = height / 2f
        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.rotate(degrees)
        rectF.set(-x + circleSpacing, -y + circleSpacing, x - circleSpacing, y - circleSpacing)
        canvas.drawArc(rectF, -45f, 270f, false, paint)
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val scaleAnim = ValueAnimator.ofFloat(1f, 0.6f, 0.5f, 1f).apply {
            duration = 750
            repeatCount = -1
        }
        addUpdateListener(scaleAnim) { animation ->
            scaleFloat = animation.animatedValue as Float
            postInvalidate()
        }

        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f).apply {
            duration = 750
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
package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import com.github.xs93.avi.Indicator


/**
 * 效果查看第2行第3个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 13:33
 * @email 466911254@qq.com
 */
class BallRotateIndicator : Indicator() {

    private var scaleFloat = 0.5f
    private var degress = 0f

    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = (width / 10).toFloat()
        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()

        canvas.rotate(degress, centerX.toFloat(), centerY.toFloat())
        canvas.save()
        canvas.translate(x - radius * 2 - radius, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.drawCircle(0f, 0f, radius, paint)
        canvas.restore()

        canvas.save()
        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.drawCircle(0f, 0f, radius, paint)
        canvas.restore()

        canvas.save()
        canvas.translate(x + radius * 2 + radius, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.drawCircle(0f, 0f, radius, paint)
        canvas.restore()
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()

        val scaleAnim = ValueAnimator.ofFloat(0.5f, 1f, 0.5f).apply {
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
            degress = animation.animatedValue as Float
            postInvalidate()
        }
        animators.add(scaleAnim)
        animators.add(rotateAnim)
        return animators
    }
}
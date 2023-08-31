package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 * 效果查看 3-4
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 13:57
 * @email 466911254@qq.com
 */
class BallTrianglePathIndicator : Indicator() {

    private var translateX = FloatArray(3)
    private var translateY = FloatArray(3)

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
        for (i in 0..2) {
            canvas.save()
            canvas.translate(translateX[i], translateY[i])
            canvas.drawCircle(0f, 0f, (width / 10).toFloat(), paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()

        val startX = (width / 5).toFloat()
        val startY = (height / 5).toFloat()
        for (i in 0..2) {
            var translateXAnim = ValueAnimator.ofFloat(width / 2f, width - startX, startX, width / 2f)
            if (i == 1) {
                translateXAnim = ValueAnimator.ofFloat(width - startX, startX, width / 2f, width - startX)
            } else if (i == 2) {
                translateXAnim = ValueAnimator.ofFloat(startX, width / 2f, width - startX, startX)
            }
            var translateYAnim = ValueAnimator.ofFloat(startY, height - startY, height - startY, startY)
            if (i == 1) {
                translateYAnim = ValueAnimator.ofFloat(height - startY, height - startY, startY, height - startY)
            } else if (i == 2) {
                translateYAnim = ValueAnimator.ofFloat(height - startY, startY, height - startY, height - startY)
            }
            translateXAnim.apply {
                duration = 2000
                interpolator = LinearInterpolator()
                repeatCount = -1
            }
            addUpdateListener(translateXAnim) { animation ->
                translateX[i] = animation.animatedValue as Float
                postInvalidate()
            }

            translateYAnim.apply {
                duration = 2000
                interpolator = LinearInterpolator()
                repeatCount = -1
            }
            addUpdateListener(translateYAnim) { animation ->
                translateY[i] = animation.animatedValue as Float
                postInvalidate()
            }

            animators.add(translateXAnim)
            animators.add(translateYAnim)
        }
        return animators
    }
}
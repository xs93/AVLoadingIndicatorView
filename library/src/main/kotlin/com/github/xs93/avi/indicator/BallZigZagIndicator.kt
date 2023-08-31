package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 * 效果查看 3-2
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 13:48
 * @email 466911254@qq.com
 */
open class BallZigZagIndicator : Indicator() {

    protected var translateX = FloatArray(2)
    protected var translateY = FloatArray(2)
    override fun draw(canvas: Canvas, paint: Paint) {
        for (i in 0..1) {
            canvas.save()
            canvas.translate(translateX[i], translateY[i])
            canvas.drawCircle(0f, 0f, (width / 10).toFloat(), paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val startX = (width / 6).toFloat()
        val startY = (height / 6).toFloat()
        for (i in 0..1) {
            var translateXAnim = ValueAnimator.ofFloat(startX, width - startX, (width / 2).toFloat(), startX)
            if (i == 1) {
                translateXAnim = ValueAnimator.ofFloat(width - startX, startX, (width / 2).toFloat(), width - startX)
            }
            var translateYAnim = ValueAnimator.ofFloat(startY, startY, (height / 2).toFloat(), startY)
            if (i == 1) {
                translateYAnim =
                    ValueAnimator.ofFloat(height - startY, height - startY, (height / 2).toFloat(), height - startY)

            }
            translateXAnim.apply {
                interpolator = LinearInterpolator()
                duration = 1000
                repeatCount = -1
            }
            addUpdateListener(translateXAnim) { animation ->
                translateX[i] = animation.animatedValue as Float
                postInvalidate()
            }

            translateYAnim.apply {
                interpolator = LinearInterpolator()
                duration = 1000
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
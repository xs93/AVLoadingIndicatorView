package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator


/**
 * 效果查看 6-2
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:40
 * @email 466911254@qq.com
 */
class BallScaleRippleMultipleIndicator : BallScaleMultipleIndicator() {

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Paint.Style.STROKE;
        paint.strokeWidth = 3f
        super.draw(canvas, paint)
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val delays = longArrayOf(0, 200, 400)
        for (i in 0..2) {
            val scaleAnim = ValueAnimator.ofFloat(0f, 1f).apply {
                interpolator = LinearInterpolator()
                duration = 1000
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }

            val alphaAnim = ValueAnimator.ofInt(0, 255).apply {
                interpolator = LinearInterpolator()
                duration = 1000
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(alphaAnim) { animation ->
                alphaInts[i] = animation.animatedValue as Int
                postInvalidate()
            }
            animators.add(scaleAnim)
            animators.add(alphaAnim)
        }
        return animators
    }
}
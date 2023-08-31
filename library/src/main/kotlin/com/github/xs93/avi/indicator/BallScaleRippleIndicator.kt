package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator


/**
 * 效果查看 6-1
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:36
 * @email 466911254@qq.com
 */
class BallScaleRippleIndicator : BallScaleIndicator() {


    override fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Paint.Style.STROKE;
        paint.strokeWidth = 3f
        super.draw(canvas, paint)
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val scaleAnim = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = LinearInterpolator()
            duration = 1000
            repeatCount = -1
        }
        addUpdateListener(scaleAnim) { animation ->
            mScale = animation.animatedValue as Float
            postInvalidate()
        }

        val alphaAnim = ValueAnimator.ofInt(0, 255).apply {
            interpolator = LinearInterpolator()
            duration = 1000
            repeatCount = -1
        }
        addUpdateListener(alphaAnim) { animation ->
            mAlpha = animation.animatedValue as Int
            postInvalidate()
        }

        animators.add(scaleAnim)
        animators.add(alphaAnim)
        return animators
    }
}
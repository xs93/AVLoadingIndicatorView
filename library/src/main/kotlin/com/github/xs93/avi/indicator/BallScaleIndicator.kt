package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 * 效果查看 4-1
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:03
 * @email 466911254@qq.com
 */
open class BallScaleIndicator : Indicator() {

    protected var mScale = 1f
    protected var mAlpha = 255

    private val circleSpacing = 4f

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.alpha = mAlpha
        canvas.scale(mScale, mScale, width / 2f, height / 2f)
        canvas.drawCircle(width / 2f, height / 2f, width / 2 - circleSpacing, paint)
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

        val alphaAnim = ValueAnimator.ofInt(255, 0).apply {
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
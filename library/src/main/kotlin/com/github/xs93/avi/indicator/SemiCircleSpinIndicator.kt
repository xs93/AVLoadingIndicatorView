package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.github.xs93.avi.Indicator


/**
 *
 * 效果查看 7-4
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 15:09
 * @email 466911254@qq.com
 */
class SemiCircleSpinIndicator : Indicator() {

    private var degrees = 0f
    private val rectF = RectF()
    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.rotate(degrees, centerX.toFloat(), centerY.toFloat())
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawArc(rectF, -60f, 120f, false, paint)
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f).apply {
            duration = 600
            repeatCount = -1

        }
        addUpdateListener(rotateAnim) {
            degrees = it.animatedValue as Float
            postInvalidate()
        }
        animators.add(rotateAnim)
        return animators
    }
}
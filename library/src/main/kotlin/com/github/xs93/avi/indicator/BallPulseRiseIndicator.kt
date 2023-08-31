package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 * 效果查看第2行第3个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 13:27
 * @email 466911254@qq.com
 */
class BallPulseRiseIndicator : Indicator() {

    private val matrix = Matrix()
    private val camera = Camera()

    private var degrees = 0f

    override fun draw(canvas: Canvas, paint: Paint) {
        matrix.reset()
        camera.save()
        camera.rotateX(degrees)
        camera.getMatrix(matrix)
        camera.restore()

        matrix.preTranslate(-centerX.toFloat(), -centerY.toFloat())
        matrix.postTranslate(centerX.toFloat(), centerY.toFloat())
        canvas.concat(matrix)

        val radius = (width / 10).toFloat()
        canvas.drawCircle((width / 4).toFloat(), radius * 2, radius, paint)
        canvas.drawCircle((width * 3 / 4).toFloat(), radius * 2, radius, paint)

        canvas.drawCircle(radius, height - 2 * radius, radius, paint)
        canvas.drawCircle((width / 2).toFloat(), height - 2 * radius, radius, paint)
        canvas.drawCircle(width - radius, height - 2 * radius, radius, paint)
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val animator = ValueAnimator.ofFloat(0f, 360f).apply {
            interpolator = LinearInterpolator()
            repeatCount = -1
            duration = 1500
        }
        addUpdateListener(animator) { animation ->
            degrees = animation.animatedValue as Float
            postInvalidate()
        }

        animators.add(animator)
        return animators
    }
}
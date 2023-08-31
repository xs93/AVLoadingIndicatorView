package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 *
 * 效果查看 7-1
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:52
 * @email 466911254@qq.com
 */
class TriangleSkewSpinIndicator : Indicator() {

    private var rotateX: Float = 0f
    private var rotateY: Float = 0f

    private val matrix = Matrix()
    private val camera = Camera()
    private val path = Path()

    override fun draw(canvas: Canvas, paint: Paint) {
        matrix.reset()
        camera.save()
        camera.rotateX(rotateX)
        camera.rotateY(rotateY)
        camera.getMatrix(matrix)
        camera.restore()
        matrix.preTranslate(-centerX.toFloat(), -centerY.toFloat())
        matrix.postTranslate(centerX.toFloat(), centerY.toFloat())
        canvas.concat(matrix)

        path.reset()
        path.moveTo(width / 5f, height * 4f / 5)
        path.lineTo(width * 4f / 5, height * 4f / 5)
        path.lineTo(width / 2f, height / 5f)
        path.close()
        canvas.drawPath(path, paint)
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val animator = ValueAnimator.ofFloat(0f, 180f, 180f, 0f, 0f).apply {
            interpolator = LinearInterpolator()
            repeatCount = -1
            duration = 2500
        }
        addUpdateListener(animator) { animation ->
            rotateX = animation.animatedValue as Float
            postInvalidate()
        }

        val animator1 = ValueAnimator.ofFloat(0f, 0f, 180f, 180f, 0f).apply {
            interpolator = LinearInterpolator()
            repeatCount = -1
            duration = 2500
        }
        addUpdateListener(animator1) { animation ->
            rotateY = animation.animatedValue as Float
            postInvalidate()
        }

        animators.add(animator)
        animators.add(animator1)
        return animators
    }
}
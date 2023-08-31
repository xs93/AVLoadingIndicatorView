package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 *
 * 效果查看 7-2
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:57
 * @email 466911254@qq.com
 */
class PacmanIndicator : Indicator() {

    private var translateX = 0f
    private var alpha = 0
    private var degrees1 = 0f
    private var degrees2: Float = 0f

    private val rectF = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {
        drawPacman(canvas, paint)
        drawCircle(canvas, paint)
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val startT = (width / 11).toFloat()

        val translationAnim = ValueAnimator.ofFloat(width - startT, (width / 2).toFloat()).apply {
            duration = 650
            interpolator = LinearInterpolator()
            repeatCount = -1
        }
        addUpdateListener(translationAnim) { animation ->
            translateX = animation.animatedValue as Float
            postInvalidate()
        }

        val alphaAnim = ValueAnimator.ofInt(255, 122).apply {
            duration = 650
            repeatCount = -1
        }
        addUpdateListener(alphaAnim) { animation ->
            alpha = animation.animatedValue as Int
            postInvalidate()
        }

        val rotateAnim1 = ValueAnimator.ofFloat(0f, 45f, 0f).apply {
            duration = 650
            repeatCount = -1
        }
        addUpdateListener(rotateAnim1) { animation ->
            degrees1 = animation.animatedValue as Float
            postInvalidate()
        }

        val rotateAnim2 = ValueAnimator.ofFloat(0f, -45f, 0f).apply {
            duration = 650
            repeatCount = -1
        }
        addUpdateListener(rotateAnim2) { animation ->
            degrees2 = animation.animatedValue as Float
            postInvalidate()
        }

        animators.add(translationAnim)
        animators.add(alphaAnim)
        animators.add(rotateAnim1)
        animators.add(rotateAnim2)
        return animators
    }

    private fun drawPacman(canvas: Canvas, paint: Paint) {
        val x = width / 2f
        val y = height / 2f
        paint.alpha = 255
        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(degrees1)
        rectF.set(-x / 1.7f, -y / 1.7f, x / 1.7f, y / 1.7f)
        canvas.drawArc(rectF, 0f, 270f, true, paint)
        canvas.restore()
        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(degrees2)
        rectF.set(-x / 1.7f, -y / 1.7f, x / 1.7f, y / 1.7f)
        canvas.drawArc(rectF, 90f, 270f, true, paint)
        canvas.restore()
    }


    private fun drawCircle(canvas: Canvas, paint: Paint) {
        val radius = (width / 11).toFloat()
        paint.alpha = alpha
        canvas.drawCircle(translateX, (height / 2).toFloat(), radius, paint)
    }
}
package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.animation.LinearInterpolator
import com.github.xs93.avi.Indicator


/**
 * 效果查看第3行第1个
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 13:37
 * @email 466911254@qq.com
 */
class CubeTransitionIndicator : Indicator() {

    private var translateX = FloatArray(2)
    private var translateY = FloatArray(2)
    private var degrees = 0f
    private var scaleFloat: Float = 1.0f

    private val rectF = RectF()

    override fun draw(canvas: Canvas, paint: Paint) {
        val rWidth = width / 5
        val rHeight = height / 5

        rectF.set(-rWidth / 2f, -rHeight / 2f, rWidth / 2f, rHeight / 2f)

        for (i in 0 until 2) {
            canvas.save()
            canvas.translate(translateX[i], translateY[i])
            canvas.rotate(degrees)
            canvas.scale(scaleFloat, scaleFloat)
            canvas.drawRect(rectF, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val startX = (width / 5).toFloat()
        val startY = (height / 5).toFloat()
        for (i in 0..1) {
            translateX[i] = startX
            var translationXAnim = ValueAnimator.ofFloat(startX, width - startX, width - startX, startX, startX)
            if (i == 1) {
                translationXAnim = ValueAnimator.ofFloat(width - startX, startX, startX, width - startX, width - startX)
            }
            translationXAnim.apply {
                interpolator = LinearInterpolator()
                duration = 1600
                repeatCount = -1
            }
            addUpdateListener(translationXAnim) {
                translateX[i] = it.animatedValue as Float
                postInvalidate()
            }

            translateY[i] = startY
            var translationYAnim = ValueAnimator.ofFloat(startY, startY, height - startY, height - startY, startY)
            if (i == 1) {
                translationYAnim =
                    ValueAnimator.ofFloat(height - startY, height - startY, startY, startY, height - startY)
            }

            translationYAnim.apply {
                interpolator = LinearInterpolator()
                duration = 1600
                repeatCount = -1
            }
            addUpdateListener(translationYAnim) {
                translateY[i] = it.animatedValue as Float
                postInvalidate()
            }

            animators.add(translationXAnim)
            animators.add(translationYAnim)
        }

        val scaleAnim = ValueAnimator.ofFloat(1f, 0.5f, 1f, 0.5f, 1f).apply {
            interpolator = LinearInterpolator()
            duration = 1600
            repeatCount = -1
        }
        addUpdateListener(scaleAnim) { animation ->
            scaleFloat = animation.animatedValue as Float
            postInvalidate()
        }

        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f, 1.5f * 360, (2 * 360).toFloat()).apply {
            interpolator = LinearInterpolator()
            duration = 1600
            repeatCount = -1
        }
        addUpdateListener(rotateAnim) { animation ->
            degrees = animation.animatedValue as Float
            postInvalidate()
        }

        animators.add(scaleAnim)
        animators.add(rotateAnim)
        return animators
    }
}
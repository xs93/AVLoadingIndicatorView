package com.github.xs93.avi.indicator

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator


/**
 * 效果查看 3-3
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 13:54
 * @email 466911254@qq.com
 */
class BallZigZagDeflectIndicator : BallZigZagIndicator() {

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val startX = (width / 6).toFloat()
        val startY = (height / 6).toFloat()
        for (i in 0..1) {
            var translateXAnim = ValueAnimator.ofFloat(startX, width - startX, startX, width - startX, startX)
            if (i == 1) {
                translateXAnim = ValueAnimator.ofFloat(width - startX, startX, width - startX, startX, width - startX)
            }
            var translateYAnim = ValueAnimator.ofFloat(startY, startY, height - startY, height - startY, startY)
            if (i == 1) {
                translateYAnim =
                    ValueAnimator.ofFloat(height - startY, height - startY, startY, startY, height - startY)
            }

            translateXAnim.apply {
                interpolator = LinearInterpolator()
                duration = 2000
                repeatCount = -1
            }
            addUpdateListener(translateXAnim) { animation ->
                translateX[i] = animation.animatedValue as Float
                postInvalidate()
            }

            translateYAnim.apply {
                interpolator = LinearInterpolator()
                duration = 2000
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
package com.github.xs93.avi.indicator

import android.animation.ValueAnimator


/**
 * 效果查看 5-3
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:33
 * @email 466911254@qq.com
 */
class LineScalePulseOutIndicator : LineScaleIndicator() {

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val delays = longArrayOf(500, 250, 0, 250, 500)
        for (i in 0..4) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.3f, 1f).apply {
                duration = 900
                repeatCount = -1
                startDelay = delays[i]
            }
            addUpdateListener(scaleAnim) { animation ->
                scaleYFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            animators.add(scaleAnim)
        }
        return animators
    }
}
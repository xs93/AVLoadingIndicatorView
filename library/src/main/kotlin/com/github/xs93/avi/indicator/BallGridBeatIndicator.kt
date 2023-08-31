package com.github.xs93.avi.indicator

import android.animation.ValueAnimator


/**
 * 效果查看 7-3
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 15:05
 * @email 466911254@qq.com
 */
class BallGridBeatIndicator : BallGridPulseIndicator() {

    override fun onCreateAnimator(): ArrayList<ValueAnimator> {
        val animators = ArrayList<ValueAnimator>()
        val durations = intArrayOf(960, 930, 1190, 1130, 1340, 940, 1200, 820, 1190)
        val delays = intArrayOf(360, 400, 680, 410, 710, -150, -120, 10, 320)

        for (i in 0..8) {
            val alphaAnim = ValueAnimator.ofInt(255, 168, 255)
            alphaAnim.duration = durations[i].toLong()
            alphaAnim.repeatCount = -1
            alphaAnim.startDelay = delays[i].toLong()
            addUpdateListener(alphaAnim) { animation ->
                alphas[i] = animation.animatedValue as Int
                postInvalidate()
            }
            animators.add(alphaAnim)
        }
        return animators
    }
}
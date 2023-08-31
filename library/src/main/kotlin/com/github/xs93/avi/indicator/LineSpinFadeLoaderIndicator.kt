package com.github.xs93.avi.indicator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF


/**
 * 效果查看 6-4
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/31 14:49
 * @email 466911254@qq.com
 */
class LineSpinFadeLoaderIndicator : BallSpinFadeLoaderIndicator() {

    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = (width / 10).toFloat()
        for (i in 0..7) {
            canvas.save()
            circleAt(width, height, width / 2.5f - radius, i * (Math.PI / 4), point)
            canvas.translate(point.x, point.y)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            canvas.rotate((i * 45).toFloat())
            paint.alpha = alphas[i]
            val rectF = RectF(-radius, -radius / 1.5f, 1.5f * radius, radius / 1.5f)
            canvas.drawRoundRect(rectF, 5f, 5f, paint)
            canvas.restore()
        }
    }
}
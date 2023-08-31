package com.github.xs93.avi

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable

/**
 * Loading 指示器绘制接口,可继承此类实现自定义指示器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/30 13:52
 * @email 466911254@qq.com
 */
abstract class Indicator : Drawable(), Animatable {

    private val mPaint = Paint(Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG)
    private var mAlpha: Int = 255

    private val mAnimators = ArrayList<ValueAnimator>()
    private val mUpdateListeners = HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener>()
    private var mHasAnimator: Boolean = false

    var color: Int
        get() = mPaint.color
        set(value) {
            mPaint.color = value
        }

    protected val drawBounds = Rect()

    val width: Int
        get() = drawBounds.width()

    val height: Int
        get() = drawBounds.height()

    val centerX: Int
        get() = drawBounds.centerX()

    val centerY: Int
        get() = drawBounds.centerY()

    val exactCenterX: Float
        get() = drawBounds.exactCenterX()

    val exactCenterY: Float
        get() = drawBounds.exactCenterY()


    init {
        mPaint.apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        draw(canvas, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mAlpha = alpha
    }

    override fun getAlpha(): Int {
        return mAlpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.OPAQUE", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun start() {
        ensureAnimator()
        if (mAnimators.isEmpty()) {
            return
        }
        if (isStarted()) {
            return
        }
        startAnimator()
        invalidateSelf()
    }

    override fun stop() {
        stopAnimator()
    }

    override fun isRunning(): Boolean {
        var running = false
        mAnimators.forEach {
            if (it.isRunning) {
                running = true
            }
        }
        return running
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        setDrawBounds(bounds)
    }

    abstract fun draw(canvas: Canvas, paint: Paint)

    abstract fun onCreateAnimator(): ArrayList<ValueAnimator>

    fun addUpdateListener(animator: ValueAnimator, listener: ValueAnimator.AnimatorUpdateListener) {
        mUpdateListeners[animator] = listener
    }

    fun setDrawBounds(drawBounds: Rect) {
        this.drawBounds.set(drawBounds)
    }

    fun setDrawBounds(left: Int, top: Int, right: Int, bottom: Int) {
        drawBounds.set(left, top, right, bottom)
    }

    fun postInvalidate() {
        invalidateSelf()
    }

    private fun ensureAnimator() {
        if (!mHasAnimator) {
            mAnimators.clear()
            mAnimators.addAll(onCreateAnimator())
            mHasAnimator = true
        }
    }

    private fun startAnimator() {
        if (mAnimators.isNotEmpty()) {
            mAnimators.forEach {
                val updateListener = mUpdateListeners[it]
                updateListener?.let { listener ->
                    it.removeUpdateListener(listener)
                    it.addUpdateListener(listener)
                }
                it.start()
            }
        }
    }

    private fun stopAnimator() {
        mAnimators.forEach {
            it.removeAllUpdateListeners()
            it.end()
        }
    }

    private fun isStarted(): Boolean {
        var started = false
        mAnimators.forEach {
            if (it.isStarted) {
                started = true
            }
        }
        return started
    }


}
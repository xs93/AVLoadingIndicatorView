package com.github.xs93.avi

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import com.github.xs93.avi.indicator.BallPulseIndicator
import kotlin.math.max
import kotlin.math.min

/**
 * loading 显示View
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/30 14:30
 * @email 466911254@qq.com
 */
class AVLoadingIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.AVLoadingIndicatorView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {

        private const val TAG = "AVLoadingIndicatorView"

        private const val MIN_SHOW_TIME = 500L
    }

    private var mMinWidth: Int
    private var mMaxWidth: Int
    private var mMinHeight: Int
    private var mMaxHeight: Int
    private var mIndicatorColor: Int

    private var mIndicator: Indicator? = null
    private var mShouldStartAnimationDrawable = false

    //防止显示和隐藏间隔时间太短,指示器显示时间过短,在显示和隐藏时按需做延迟处理
    private var mStartShowTime = -1L
    private var mPostHide: Boolean = false
    private var mDismissed: Boolean = false

    private val mDelayHide: Runnable = Runnable {
        mPostHide = false
        mStartShowTime = -1
        visibility = GONE
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AVLoadingIndicatorView, defStyleAttr, defStyleRes)
        mMinWidth = ta.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_minWidth, 24)
        mMaxWidth = ta.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_maxWidth, 48)
        mMinHeight = ta.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_minHeight, 24)
        mMaxHeight = ta.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_maxHeight, 48)
        mIndicatorColor = ta.getColor(R.styleable.AVLoadingIndicatorView_indicatorColor, Color.WHITE)
        val indicatorName = ta.getString(R.styleable.AVLoadingIndicatorView_indicatorName)
        setIndicator(indicatorName)
        if (mIndicator == null) {
            setIndicator(BallPulseIndicator())
        }
        ta.recycle()
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var dw = 0
        var dh = 0
        val drawable = mIndicator
        drawable?.let {
            dw = max(mMinWidth, min(mMaxWidth, it.intrinsicWidth))
            dh = max(mMinHeight, min(mMaxHeight, it.intrinsicHeight))
        }

        updateDrawableState()

        dw += (paddingLeft + paddingRight)
        dh += (paddingTop + paddingBottom)

        val measureWidth = resolveSizeAndState(dw, widthMeasureSpec, 0)
        val measureHeight = resolveSizeAndState(dh, heightMeasureSpec, 0)
        setMeasuredDimension(measureWidth, measureHeight)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        uploadDrawableBounds(w, h)
    }


    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIndicator(canvas)
    }

    override fun setVisibility(visibility: Int) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility)
            if (visibility == GONE || visibility == INVISIBLE) {
                stopAnimation()
            } else {
                startAnimation()
                mStartShowTime = SystemClock.elapsedRealtime()
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation()
        } else {
            startAnimation()
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who == mIndicator || super.verifyDrawable(who)
    }

    override fun invalidateDrawable(drawable: Drawable) {
        if (verifyDrawable(drawable)) {
            val dirty = drawable.bounds
            val scrollX = scrollX + paddingLeft
            val scrollY = scrollY + paddingTop
            invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY)
        } else {
            super.invalidateDrawable(drawable)
        }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        updateDrawableState()
    }

    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        mIndicator?.setHotspot(x, y)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
        removeCallback()
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
        removeCallback()
    }

    fun setIndicator(indicator: Indicator?) {
        if (mIndicator != indicator) {
            mIndicator?.let {
                it.callback = null
                it.stop()
                unscheduleDrawable(it)
            }

            mIndicator = indicator
            setIndicatorColor(mIndicatorColor)
            indicator?.let {
                it.callback = this
            }
            if (width != 0 && height != 0) {
                uploadDrawableBounds(width, height)
            }
            if (isAttachedToWindow) {
                startAnimation()
            } else {
                postInvalidate()
            }
        }
    }

    fun setIndicator(indicatorName: String?) {
        if (indicatorName.isNullOrBlank()) {
            return
        }
        val drawableClassName = StringBuilder()
        if (!indicatorName.contains(".")) {
            val defaultPackageName = javaClass.`package`?.name
            drawableClassName.append(defaultPackageName)
                .append(".indicator")
                .append(".")
        }
        drawableClassName.append(indicatorName)

        try {
            val drawableClass = Class.forName(drawableClassName.toString())
            val indicator = drawableClass.newInstance() as Indicator
            setIndicator(indicator)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "Didn't find your class , check the name again !")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setIndicatorColor(color: Int) {
        mIndicatorColor = color
        mIndicator?.color = color
    }

    fun smoothToShow() {
        if (visibility == VISIBLE) {
            return
        }
        startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
        visibility = VISIBLE
    }

    fun smoothToHide() {
        if (visibility == GONE) {
            return
        }

        val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out).apply {
            setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    visibility = GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
        }
        startAnimation(animation)
    }

    fun show() {
        mDismissed = false
        visibility = VISIBLE
    }

    fun hide() {
        mDismissed = true
        val diff = SystemClock.elapsedRealtime() - mStartShowTime
        if (diff >= MIN_SHOW_TIME || mStartShowTime == -1L) {
            visibility = GONE
        } else {
            if (!mPostHide) {
                postDelayed(mDelayHide, MIN_SHOW_TIME - diff)
                mPostHide = true
            }
        }
    }

    fun startAnimation() {
        if (visibility != VISIBLE) {
            return
        }
        if (mIndicator is Animatable) {
            mShouldStartAnimationDrawable = true
        }
        postInvalidate()
    }

    fun stopAnimation() {
        mIndicator?.let {
            it.stop()
            mShouldStartAnimationDrawable = false
        }
        postInvalidate()
    }

    private fun uploadDrawableBounds(w: Int, h: Int) {
        val useWidth = w - paddingLeft - paddingRight
        val useHeight = h - paddingTop - paddingBottom
        var left = 0
        var top = 0
        var right = useWidth
        var bottom = useHeight
        mIndicator?.let {
            val intrinsicWidth = it.intrinsicWidth
            val intrinsicHeight = it.intrinsicHeight
            val intrinsicAspect = intrinsicWidth.toFloat() / intrinsicHeight
            val boundAspect = useWidth.toFloat() / useHeight
            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    val width = (h * intrinsicAspect).toInt()
                    left = (useWidth - width) / 2
                    right = left + width
                } else {
                    val height = (w * (1f / intrinsicAspect)).toInt()
                    top = (useHeight - height) / 2
                    bottom = top + height
                }
            }
        }
        mIndicator?.setBounds(left, top, right, bottom)
    }

    private fun drawIndicator(canvas: Canvas) {
        mIndicator?.let {
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            it.draw(canvas)
            canvas.restoreToCount(saveCount)
            if (mShouldStartAnimationDrawable) {
                it.start()
                mShouldStartAnimationDrawable = false
            }
        }
    }

    private fun updateDrawableState() {
        val state = drawableState
        mIndicator?.let {
            if (it.isStateful) {
                it.state = state
            }
        }
    }

    private fun removeCallback() {
        removeCallbacks(mDelayHide)
    }
}
package com.swolfand.ticktock.widgets

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.widget.FrameLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.swolfand.ticktock.R
import com.swolfand.ticktock.widgets.ExpandableLayout.State.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private const val ZERO = 0F
private const val ONE = 1F

private const val DEFAULT_DURATION = 300

/**
 * An implementation of a vertically expandable layout.
 */
class ExpandableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val keySuperState = "${ExpandableLayout::class.java.simpleName}.key_super_state"
    private val keyExpansion = "${ExpandableLayout::class.java.simpleName}.key_expansion"

    enum class State {
        COLLAPSED,
        COLLAPSING,
        EXPANDING,
        EXPANDED
    }

    private var duration: Int = DEFAULT_DURATION
    private var parallax: Float = ONE
        set(value) {
            min(ONE, max(ZERO, value))
        }
    private var expansion: Float = ZERO
    private lateinit var state: State

    private var interpolator: Interpolator = FastOutSlowInInterpolator()
    private var animator: ValueAnimator? = null

    val isExpanded: Boolean get() = state in arrayOf(State.EXPANDING, EXPANDED)

    init {
        attrs?.let {
            val a = getContext().obtainStyledAttributes(it, R.styleable.ExpandableLayout)
            duration = a.getInt(R.styleable.ExpandableLayout_duration, DEFAULT_DURATION)
            expansion =
                if (a.getBoolean(R.styleable.ExpandableLayout_expanded, false)) ONE else ZERO
            parallax = a.getFloat(R.styleable.ExpandableLayout_parallax, ONE)
            a.recycle()

            state = if (expansion == ZERO) COLLAPSED else EXPANDED
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        expansion = if (isExpanded) ONE else ZERO

        return Bundle().apply {
            putFloat(keyExpansion, expansion)
            putParcelable(keySuperState, superState)
        }
    }

    override fun onRestoreInstanceState(parcelable: Parcelable) {
        (parcelable as? Bundle)?.run {
            expansion = getFloat(keyExpansion)
            state = if (expansion == ONE) EXPANDED else COLLAPSED
            val superState = getParcelable<Parcelable>(keySuperState)
            super.onRestoreInstanceState(superState)
        } ?: super.onRestoreInstanceState(parcelable)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val height = measuredHeight

        visibility = if (expansion == ZERO && height == ZERO.toInt()) View.GONE else View.VISIBLE

        val expansionDelta: Int = height - (height * expansion).roundToInt()
        if (parallax > ZERO) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.translationY = -parallaxDelta
            }
        }

        setMeasuredDimension(measuredWidth, height - expansionDelta)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        animator?.cancel()
        super.onConfigurationChanged(newConfig)
    }

    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) {
            collapse(animate)
        } else {
            expand(animate)
        }
    }

    @JvmOverloads
    fun expand(animate: Boolean = true) {
        setExpanded(true, animate)
    }

    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        setExpanded(false, animate)
    }

    private fun setExpanded(expand: Boolean, animate: Boolean = true) {
        if (expand == isExpanded) {
            return
        }

        val targetExpansion = if (expand) ONE else ZERO
        if (animate) {
            animateSize(targetExpansion)
        } else {
            setExpansion(targetExpansion)
        }
    }

    fun setExpansion(expansion: Float) {
        if (this.expansion == expansion) {
            return
        }

        val delta = expansion - this.expansion
        state = when {
            expansion == ZERO -> COLLAPSED
            expansion == ONE -> EXPANDED
            delta < ZERO -> COLLAPSING
            else -> EXPANDING
        }

        visibility = if (state == COLLAPSED) View.GONE else View.VISIBLE
        this.expansion = expansion
        requestLayout()
    }

    private fun animateSize(targetExpansion: Float) {
        animator?.let {
            animator?.cancel()
            animator = null
        }

        animator = ValueAnimator.ofFloat(expansion, targetExpansion).apply {
            this.interpolator = this@ExpandableLayout.interpolator
            this.duration = this@ExpandableLayout.duration.toLong()
            addUpdateListener { valueAnimator -> setExpansion(valueAnimator.animatedValue as Float) }
            addListener(ExpansionListener(targetExpansion))
            start()
        }
    }

    private inner class ExpansionListener(private val targetExpansion: Float) :
        Animator.AnimatorListener {
        private var canceled: Boolean = false

        override fun onAnimationStart(animation: Animator) {
            state = if (targetExpansion == ZERO) COLLAPSING else EXPANDING
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                state = if (targetExpansion == ZERO) COLLAPSED else EXPANDED
                setExpansion(targetExpansion)
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator) = Unit
    }
}

package com.colombia.credit.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.updateLayoutParams


class AnimtorUtils {

    companion object {

        fun startAnima(
            view: View,
            startValue: Float,
            endValue: Float,
            height: Int,
            duration: Long = 300,
            animEndBody: () -> Unit
        ) {
            val animator = ValueAnimator.ofFloat(startValue, endValue)
            animator.duration = duration
            animator.addUpdateListener { animation ->
                view.updateLayoutParams {
                    val value = animation.animatedValue as Float
                    this.height = (value * height).toInt()
                }
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    animEndBody()
                    if (endValue == 0f) {
                        view.visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            animator.start()
        }

        fun startAnima(
            view: ViewGroup,
            startValue: Float,
            endValue: Float,
            height: Int,
            duration: Long = 300,
            animEndBody: () -> Unit
        ) {
            if (view.visibility == View.GONE) {
                view.visibility = View.VISIBLE
            }
            val animator = ValueAnimator.ofFloat(startValue, endValue)
            animator.duration = duration
            animator.interpolator = AccelerateInterpolator()
            animator.addUpdateListener { animation ->
                view.updateLayoutParams {
                    val value = animation.animatedValue as Float
                    this.height = (value * height).toInt()
                }
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    animEndBody()
                    if (endValue == 0f) {
                        view.visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            animator.start()
        }
    }
}
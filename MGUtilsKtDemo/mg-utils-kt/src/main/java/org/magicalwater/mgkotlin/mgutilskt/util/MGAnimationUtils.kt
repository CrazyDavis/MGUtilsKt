package org.magicalwater.mgkotlin.mgutilskt.util

import android.animation.Animator
import android.view.animation.Animation
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.view.View

data class MGAnimationAttr(val name: String, val start: Float, val end: Float)

/**
 * Created by 志朋 on 2017/11/21.
 */
class MGAnimationUtils private constructor() {

    companion object {
        val NAME_SCALE_X = "scaleX"
        val NAME_SCALE_Y = "scaleY"
        val NAME_ALPHA = "alpha"
        val NAME_TRANSLATE_X = "translationX"
        val NAME_TRANSLATE_Y = "translationY"
        val NAME_ROTATE = "rotation"
        val NAME_ROTATE_X = "rotationX"
        val NAME_ROTATE_Y = "rotationY"

        fun animator(view: View, attrs: List<MGAnimationAttr>, duration: Int, listener: Animator.AnimatorListener? = null) {

            MGLogUtils.d("秀動畫")
            val animatorCollect = attrs.map { ObjectAnimator.ofFloat(view, it.name, it.start, it.end) }

            val animatorSet = AnimatorSet()
            animatorSet.duration = duration.toLong()
            animatorSet.addListener(listener)
            animatorSet.playTogether(animatorCollect)
            animatorSet.start()
        }


    }



}
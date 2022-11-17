package com.rouxinpai.arms.extension

import android.view.View
import android.view.animation.AnimationUtils
import com.rouxinpai.arms.R

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/8/6 10:20
 * desc   :
 */

/**
 * 控件抖动动画
 */
fun View.shake() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_anim))
}
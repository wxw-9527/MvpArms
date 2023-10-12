package com.rouxinpai.arms.extension

import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.viewpager2.widget.ViewPager2
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

/**
 *
 */
fun EditText.setTextMoveSelection(text: String?) {
    setText(text)
    setSelection(text?.length ?: 0)
}

/**
 * 设置左、上、右和下方的图标
 */
fun TextView.setDrawable(
    @DrawableRes left: Int = 0,
    @DrawableRes top: Int = 0,
    @DrawableRes right: Int = 0,
    @DrawableRes bottom: Int = 0,
) {
    setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}


/**
 * 设置左、上、右和下方的图标
 */
fun TextView.setDrawable(
    left: Drawable? = null,
    top: Drawable? = null,
    right: Drawable? = null,
    bottom: Drawable? = null,
) {
    setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}

/**
 * 清空控件数据及tag
 */
fun TextView.clear() {
    text = null
    tag = null
}

/**
 * 设置[EditText]是否启用/禁用
 */
fun EditText.setEnable(enable: Boolean) {
    isEnabled = enable
    isFocusable = enable
    isFocusableInTouchMode = enable
    isClickable = enable
    isLongClickable = enable
}

/**
 * 启用[EditText]
 */
fun EditText.enable() = setEnable(true)

/**
 * 禁用[EditText]
 */
fun EditText.disabled() = setEnable(false)

/**
 * 下一页
 */
fun ViewPager2.next() {
    currentItem = ++currentItem
}

/**
 * 上一页
 */
fun ViewPager2.previous() {
    currentItem = --currentItem
}
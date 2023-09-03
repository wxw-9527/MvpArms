package com.rouxinpai.arms.view

import android.view.View
import android.view.View.OnClickListener

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/3 15:25
 * desc   :
 */
abstract class OnDebounceClickListener(private val delayTime: Long = 500L) : OnClickListener {

    // 最后一次点击时间
    private var mLastClickTime: Long = 0L

    private fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        val timeLength = time - mLastClickTime
        if (timeLength in 1 until delayTime) {
            return true
        }
        mLastClickTime = time
        return false
    }

    override fun onClick(v: View?) {
        // 判断当前点击事件与前一次点击事件时间间隔是否小于阙值
        if (isFastDoubleClick()) return
        onDebounceClick(v)
    }

    /**
     * 防抖点击事件回调方法
     * @param v
     */
    abstract fun onDebounceClick(v: View?)
}
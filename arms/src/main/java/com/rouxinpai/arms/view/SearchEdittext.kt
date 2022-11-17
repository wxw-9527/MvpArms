package com.rouxinpai.arms.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/9/1 17:10
 * desc   :
 */
class SearchEdittext @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {

        private const val LIMIT = 500L
    }

    private var mListener: OnTextChangedListener? = null
    private var mStartText: String = ""
    private val mAction = Runnable {
        val listener = mListener
        if (listener != null) {
            // 判断最终和开始前是否一致
            if (mStartText != text.toString()) {
                mStartText = text.toString() // 更新 mStartText
                listener.onTextChanged(mStartText)
            }
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        // 移除上一次的回调
        removeCallbacks(mAction)
        postDelayed(mAction, if (text.isNullOrBlank()) 0L else LIMIT)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(mAction)
    }

    /**
     *
     */
    fun setOnTextChangedListener(listener: OnTextChangedListener) {
        mListener = listener
    }

    /**
     *
     */
    interface OnTextChangedListener {
        fun onTextChanged(text: String)
    }
}
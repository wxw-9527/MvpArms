package com.rouxinpai.arms.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/9/1 17:10
 * desc   :
 */
class SearchEdittext @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle,
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {

        private const val LIMIT = 500L
    }

    private var mDisposable: Disposable? = null

    private var mListener: OnTextChangedListener? = null

    init {
        mDisposable = afterTextChangeEvents()
            .debounce(LIMIT, TimeUnit.MILLISECONDS)
            .filter { event ->
                val editable = event.editable
                !editable.isNullOrEmpty()
            } // 过滤掉空字符串
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                mListener?.onTextChanged(this, text.editable?.toString())
            }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int,
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.isNullOrEmpty()) {
            mListener?.onTextChanged(this, text?.toString())
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mDisposable?.dispose()
        mDisposable = null
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
        fun onTextChanged(v: View, text: String?)
    }
}
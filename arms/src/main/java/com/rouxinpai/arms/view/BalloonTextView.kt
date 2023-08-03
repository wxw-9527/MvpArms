package com.rouxinpai.arms.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/3 16:05
 * desc   :
 */
class BalloonTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr), View.OnClickListener {

    //
    private var mBalloon: Balloon? = null

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (text.isNullOrEmpty()) return // 文本内容为空不弹出提示
        if (mBalloon == null) {
            mBalloon = Balloon.Builder(context)
                .setText(text)
                .setPaddingHorizontal(8)
                .setPaddingVertical(3)
                .setCornerRadius(2f)
                .setArrowSize(6)
                .setTextSize(14f)
                .setAutoDismissDuration(3 * 1000L)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .build()
        }
        mBalloon?.showAlignTop(this)
    }
}
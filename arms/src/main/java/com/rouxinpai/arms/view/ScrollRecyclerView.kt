package com.rouxinpai.arms.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.dialogx.interfaces.ScrollController

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/16 23:13
 * desc   : 自定义滑动布局。实现 [ScrollController]，[com.kongzue.dialogx.dialogs.BottomDialog]根据此接口给出的数据进行滑动事件拦截的处理和判断
 */
class ScrollRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.recyclerview.R.attr.recyclerViewStyle,
) : RecyclerView(context, attrs, defStyleAttr), ScrollController {

    /**
     * 滑动锁定判断依据，若此标记被置为 true，则意味着滑动由父布局处理，请勿进行任何滚动操作。
     * 具体请参考 {@link #onTouchEvent(MotionEvent)} 的处理方案，其他诸如 ScrollView 处理方式相同。
     */
    private var mLockScroll: Boolean = false

    override fun getScrollDistance(): Int {
        val layoutManager = layoutManager as LinearLayoutManager
        val firstVisibleItem = this.getChildAt(0)
        return if (firstVisibleItem != null) {
            val itemHeight = firstVisibleItem.height
            val firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem)
            val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
            (firstItemPosition + 1) * itemHeight - firstItemBottom
        } else {
            0
        }
    }

    override fun isCanScroll(): Boolean {
        return canScrollVertically(1) || canScrollVertically(-1)
    }

    override fun lockScroll(lockScroll: Boolean) {
        this.mLockScroll = lockScroll
    }

    override fun isLockScroll(): Boolean {
        return mLockScroll
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (mLockScroll) {
            return false
        }
        return super.onTouchEvent(e)
    }
}
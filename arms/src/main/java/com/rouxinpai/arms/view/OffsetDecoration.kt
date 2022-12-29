package com.rouxinpai.arms.view

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/12/29 16:57
 * desc   :
 */
class OffsetDecoration(
    private val size: Float,
    private val sizeUnit: Int = TypedValue.COMPLEX_UNIT_DIP
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val dataSize = state.itemCount
        val position = parent.getChildAdapterPosition(view)
        if (dataSize > 0 && position == dataSize - 1) {
            val displayMetrics = view.context.resources.displayMetrics
            val bottom = TypedValue.applyDimension(sizeUnit, size, displayMetrics).roundToInt()
            outRect.set(0, 0, 0, bottom)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }
}
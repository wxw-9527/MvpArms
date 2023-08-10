package com.rouxinpai.arms.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.databinding.DropdownButtonRecycleItemBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/22 21:48
 * desc   :
 */
class DropdownButton<T : DropdownButton.IDropdownEntity> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr),
    OnClickListener {

    /**
     *
     */
    interface IDropdownEntity {
        fun getShowText(): String
    }

    /**
     *
     */
    interface OnItemSelectedListener {
        fun onItemSelected(view: View, position: Int, item: IDropdownEntity)
    }

    // 菜单列表适配器实例
    private val mMenuAdapter: MenuAdapter<T>

    // 监听事件
    private var mOnItemSelectedListener: OnItemSelectedListener? = null

    init {
        // 实例化菜单列表适配器
        mMenuAdapter = MenuAdapter()
        // 绑定监听事件
        setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (mMenuAdapter.data.isEmpty()) {
            return
        }
        CustomDialog.show(mOnBindView)
            .setCancelable(true)
            .setMaskColor(ContextCompat.getColor(context, R.color.cl_dropdown_button_dialog_mask))
            .setAlignBaseViewGravity(this, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            .setBaseViewMarginTop(SizeUtils.dp2px(2f))
            .setEnterAnimResId(com.kongzue.dialogx.R.anim.anim_dialogx_top_enter)
            .setExitAnimResId(com.kongzue.dialogx.R.anim.anim_dialogx_top_exit)
            .show()
    }

    //
    private val mOnBindView = object : OnBindView<CustomDialog>(R.layout.dropdown_button_dialog) {

        override fun onBind(dialog: CustomDialog, v: View) {
            // 获取弹窗根布局实例
            val rootView = v.findViewById<ConstraintLayout>(R.id.root_view)
            // 动态设置宽度
            val layoutParams = rootView.layoutParams
            layoutParams.width = width
            rootView.layoutParams = layoutParams
            // 绑定适配器
            val recyclerView = v.findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = mMenuAdapter
            // 绑定监听事件
            mMenuAdapter.setOnItemClickListener { _, _, position ->
                val selectedIndex = mMenuAdapter.getSelectedIndex()
                if (selectedIndex != position) {
                    selectItemByPosition(position)
                }
                dialog.dismiss()
            }
        }
    }

    /**
     * 重置状态
     */
    fun clear() {
        // 清除文案及标签
        text = null
        tag = null
        // 清除选中下标
        mMenuAdapter.clearSelectedIndex()
        // 清除数据
        mMenuAdapter.data.clear()
    }

    /**
     * 填充列表数据
     */
    fun setList(list: Collection<T>?) {
        if (list != null) {
            mMenuAdapter.setList(list)
        }
    }

    /**
     * 绑定监听事件
     */
    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        mOnItemSelectedListener = listener
    }

    /**
     *
     */
    fun selectItemByPosition(position: Int) {
        // 获取选中项
        val item = mMenuAdapter.getItemOrNull(position = position)
        if (item != null) {
            // 渲染界面数据
            text = item.getShowText()
            // 设置选中
            mMenuAdapter.select(position)
            // 调用回调函数
            mOnItemSelectedListener?.onItemSelected(this, position, item)
        }
    }

    // 菜单列表适配器
    private class MenuAdapter<T : IDropdownEntity> :
        BaseVbAdapter<DropdownButtonRecycleItemBinding, T>() {

        // 选中项下标
        private var mSelectedIndex = -1

        /**
         *
         */
        fun getSelectedIndex() = mSelectedIndex

        /**
         *
         */
        @SuppressLint("NotifyDataSetChanged")
        fun select(index: Int) {
            mSelectedIndex = index
            notifyDataSetChanged()
        }

        fun clearSelectedIndex() {
            mSelectedIndex = -1
        }

        override fun onCreateViewBinding(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ): DropdownButtonRecycleItemBinding {
            return DropdownButtonRecycleItemBinding.inflate(inflater, parent, false)
        }

        override fun onBindView(
            binding: DropdownButtonRecycleItemBinding,
            position: Int,
            item: T
        ) {
            // 设置选中标志
            val backgroundColor = if (mSelectedIndex == position) {
                ContextCompat.getColor(context, R.color.cl_dropdown_button_selected_item)
            } else {
                Color.TRANSPARENT
            }
            binding.tvText.setBackgroundColor(backgroundColor)
            // 渲染文本内容
            binding.tvText.text = item.getShowText()
        }
    }
}
package com.rouxinpai.arms.extension

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.bumptech.glide.Glide
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.adapter.BaseAdapter
import com.rouxinpai.arms.databinding.NineGridViewItemBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/21 10:24
 * desc   :
 */
class NineGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.gridViewStyle,
) : GridView(context, attrs, defStyleAttr),
    AdapterView.OnItemClickListener,
    BaseAdapter.OnItemChildClickListener<String> {

    companion object {
        private const val DEFAULT_EDITABLE = false
        private const val DEFAULT_MAX_ITEM_COUNT = 9
    }

    // 是否允许编辑
    private var mEditable: Boolean = DEFAULT_EDITABLE

    // 图片最大张数
    private var mMaxItemCount: Int = DEFAULT_MAX_ITEM_COUNT

    // 适配器
    private val mAdapter: NineAdapter

    // 添加按钮点击事件监听
    private var mOnAddClickListener: OnAddClickListener? = null

    // 删除按钮点击事件监听
    private var mOnDeleteClickListener: OnDeleteClickListener? = null

    init {
        // 初始化自定义参数
        context.obtainStyledAttributes(attrs, R.styleable.NineGridView).use {
            mEditable = it.getBoolean(R.styleable.NineGridView_ngv_editable, DEFAULT_EDITABLE)
            mMaxItemCount =
                it.getInt(R.styleable.NineGridView_ngv_maxItemCount, DEFAULT_MAX_ITEM_COUNT)
        }
        // 绑定适配器
        mAdapter = NineAdapter(mMaxItemCount).apply { editable = mEditable }
        adapter = mAdapter
        // 绑定监听事件
        onItemClickListener = this
        mAdapter.setOnItemChildClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (mAdapter.editable && position >= mAdapter.dataSize) {
            mOnAddClickListener?.onAddClick()
        }
    }

    override fun onItemChildClick(view: View, position: Int, item: String) {
        if (R.id.btn_delete == view.id) {
            mAdapter.removeAt(position)
            mOnDeleteClickListener?.onDeleteClick(position)
        }
    }

    val missingCount: Int
        get() = mMaxItemCount - mAdapter.dataSize

    val data: List<String>
        get() = mAdapter.getList()

    fun addData(data: String?) {
        if (data.isNullOrEmpty()) return
        mAdapter.addItem(data)
    }

    fun addData(list: List<String>?) {
        if (list.isNullOrEmpty()) return
        mAdapter.addItems(list)
    }

    fun setNewList(list: List<String>?) {
        if (list.isNullOrEmpty()) return
        mAdapter.setNewList(list)
    }

    fun setEditable(editable: Boolean) {
        mAdapter.editable = editable
    }

    fun setOnAddClickListener(listener: OnAddClickListener) {
        mOnAddClickListener = listener
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        mOnDeleteClickListener = listener
    }

    interface OnAddClickListener {
        fun onAddClick()
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    /**
     * 适配器
     */
    private class NineAdapter(val mMaxItemCount: Int) :
        BaseAdapter<NineGridViewItemBinding, String>() {

        init {
            addChildClickViewIds(R.id.btn_delete)
        }

        var editable: Boolean = false
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getCount(): Int {
            if (editable && dataSize < mMaxItemCount) {
                return dataSize.plus(1)
            }
            return super.getCount()
        }

        override fun createBinding(
            inflater: LayoutInflater,
            parent: ViewGroup?,
        ): NineGridViewItemBinding {
            return NineGridViewItemBinding.inflate(inflater, parent, false)
        }

        override fun bindView(binding: NineGridViewItemBinding, position: Int) {
            // 允许编辑
            if (editable) {
                // 已添加图片数量未达上限
                if (dataSize < mMaxItemCount) {
                    // 展示已添加图片
                    if (position < dataSize) {
                        binding.btnDelete.visibility = View.VISIBLE
                        Glide.with(binding.ivImage).load(getItem(position)).into(binding.ivImage)
                    }
                    // 展示添加图片按钮
                    else {
                        binding.btnDelete.visibility = View.INVISIBLE
                        Glide.with(binding.ivImage).load(R.drawable.nine_grid_view__ic_plus)
                            .into(binding.ivImage)
                    }
                }
                // 图片数量达到上限
                else {
                    binding.btnDelete.visibility = View.VISIBLE
                    Glide.with(binding.ivImage).load(getItem(position)).into(binding.ivImage)
                }
            } else {
                binding.btnDelete.visibility = View.INVISIBLE
                Glide.with(binding.ivImage).load(getItem(position)).into(binding.ivImage)
            }
        }
    }
}
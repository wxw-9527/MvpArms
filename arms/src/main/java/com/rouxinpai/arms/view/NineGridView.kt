package com.rouxinpai.arms.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import androidx.core.content.res.use
import com.bumptech.glide.Glide
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.adapter.BaseAdapter
import com.rouxinpai.arms.databinding.NineGridViewItemBinding
import com.rouxinpai.arms.extension.show

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/21 10:24
 * desc   :
 */
class NineGridView<T : NineGridView.IEntity> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.gridViewStyle,
) : GridView(context, attrs, defStyleAttr),
    AdapterView.OnItemClickListener,
    BaseAdapter.OnItemChildClickListener<T> {

    companion object {
        private const val DEFAULT_EDITABLE = false
        private const val DEFAULT_MAX_ITEM_COUNT = 9
    }

    // 是否允许编辑
    private var mEditable: Boolean = DEFAULT_EDITABLE

    // 图片最大张数
    private var mMaxItemCount: Int = DEFAULT_MAX_ITEM_COUNT

    // 适配器
    private val mAdapter: NineAdapter<T>

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
        mAdapter = NineAdapter<T>(mMaxItemCount).apply { editable = mEditable }
        adapter = mAdapter
        // 绑定监听事件
        onItemClickListener = this
        mAdapter.setOnItemChildClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position >= mAdapter.dataSize) {
            if (mAdapter.editable) {
                mOnAddClickListener?.onAddClick()
            }
        } else {
            this.show(
                R.id.iv_image,
                mAdapter.getList().map { it.path },
                position
            )
        }
    }

    override fun onItemChildClick(view: View, position: Int, item: T) {
        if (R.id.btn_delete == view.id) {
            mAdapter.removeAt(position)
            mOnDeleteClickListener?.onDeleteClick(position)
        }
    }

    val maxItemCount: Int
        get() = mMaxItemCount

    val missingCount: Int
        get() = mMaxItemCount - mAdapter.dataSize

    val data: List<T>
        get() = mAdapter.getList()

    fun addData(data: T?) {
        if (data == null) return
        mAdapter.addItem(data)
    }

    fun addData(list: List<T>?) {
        if (list.isNullOrEmpty()) return
        mAdapter.addItems(list)
    }

    fun setNewList(list: List<T>?) {
        mAdapter.setNewList(list.orEmpty())
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
     *
     */
    interface IEntity {
        val path: String
    }

    /**
     * 适配器
     */
    private class NineAdapter<T : IEntity>(val mMaxItemCount: Int) : BaseAdapter<NineGridViewItemBinding, T>() {

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

        override fun bindView(binding: NineGridViewItemBinding, position: Int) {
            // 允许编辑
            if (editable) {
                // 已添加图片数量未达上限
                if (dataSize < mMaxItemCount) {
                    // 展示已添加图片
                    if (position < dataSize) {
                        binding.btnDelete.visibility = View.VISIBLE
                        Glide.with(binding.ivImage).load(getItem(position).path).into(binding.ivImage)
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
                    Glide.with(binding.ivImage).load(getItem(position).path).into(binding.ivImage)
                }
            } else {
                binding.btnDelete.visibility = View.INVISIBLE
                Glide.with(binding.ivImage).load(getItem(position).path).into(binding.ivImage)
            }
        }
    }
}
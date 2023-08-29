package com.rouxinpai.arms.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import android.widget.BaseAdapter
import androidx.annotation.IdRes
import com.rouxinpai.arms.util.BindingReflex

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/21 10:21
 * desc   :
 */
abstract class BaseAdapter<VB : ViewBinding, T> : BaseAdapter() {

    /* ************ 常量 ************ */

    /* ************ 字段 ************ */

    // 数据集
    private val mList = arrayListOf<T>()

    // 用于保存需要设置点击事件的 item
    private val mChildClickViewIds = LinkedHashSet<Int>()

    // 子控件点击事件监听
    private var mOnItemChildClickListener: OnItemChildClickListener<T>? = null

    /* ************ 构造函数 ************ */

    /* ************ 重写函数 ************ */

    override fun getCount(): Int = mList.size

    override fun getItem(position: Int): T = mList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val binding: VB
        if (null == view) {
            binding = BindingReflex.reflexViewBinding(this::class.java, LayoutInflater.from(parent?.context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            @Suppress("UNCHECKED_CAST")
            binding = view.tag as VB
        }
        bindView(binding, position)
        bindItemChildClickListener(view, position)
        return view
    }

    /* ************ 回调 ************ */

    /* ************ 公共函数 ************ */

    abstract fun bindView(binding: VB, position: Int)

    fun getList(): List<T> = mList

    val dataSize: Int get() = mList.size

    fun setNewList(collection: Collection<T>) {
        mList.clear()
        mList.addAll(collection)
        notifyDataSetChanged()
    }

    fun addItem(data: T) {
        mList.add(data)
        notifyDataSetChanged()
    }

    fun addItems(collection: Collection<T>) {
        mList.addAll(collection)
        notifyDataSetChanged()
    }

    fun remove(data: T) {
        mList.remove(data)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun addChildClickViewIds(@IdRes vararg viewIds: Int) {
        viewIds.forEach {
            mChildClickViewIds.add(it)
        }
    }

    fun setOnItemChildClickListener(listener: OnItemChildClickListener<T>) {
        mOnItemChildClickListener = listener
    }

    /* ************ 私有函数 ************ */

    private fun bindItemChildClickListener(view: View, position: Int) {
        mOnItemChildClickListener?.let { listener ->
            mChildClickViewIds.forEach { id ->
                val childView: View = view.findViewById(id)
                childView.setOnClickListener {
                    listener.onItemChildClick(childView, position, getItem(position))
                }
            }
        }
    }

    /* ************ 内部类或接口 ************ */

    interface OnItemChildClickListener<T> {
        fun onItemChildClick(view: View, position: Int, item: T)
    }
}
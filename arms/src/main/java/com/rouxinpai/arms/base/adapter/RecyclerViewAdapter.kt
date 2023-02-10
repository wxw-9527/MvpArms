package com.rouxinpai.arms.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:32
 * desc   :
 */

open class VbHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

abstract class BaseVbAdapter<VB : ViewBinding, T> : BaseQuickAdapter<T, VbHolder<VB>>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VbHolder<VB> {
        val binding = onCreateViewBinding(LayoutInflater.from(parent.context), parent, viewType)
        return VbHolder(binding)
    }

    override fun onBindViewHolder(holder: VbHolder<VB>, position: Int, item: T?) {
        if (item == null) return
        onBindView(holder.binding, position, item)
    }

    override fun onBindViewHolder(
        holder: VbHolder<VB>,
        position: Int,
        item: T?,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, item, payloads)
        if (item == null) return
        onBindView(holder.binding, position, item, payloads)
    }

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VB

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

abstract class OnMultiItemAdapterListener<T, VB : ViewBinding> : BaseMultiItemAdapter.OnMultiItemAdapterListener<T, VbHolder<VB>> {

    override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): VbHolder<VB> {
        val binding = onCreateViewBinding(LayoutInflater.from(parent.context), parent, viewType)
        return VbHolder(binding)
    }

    override fun onBind(holder: VbHolder<VB>, position: Int, item: T?) {
        if (item == null) return
        onBindView(holder.binding, position, item)
    }

    override fun onBind(holder: VbHolder<VB>, position: Int, item: T?, payloads: List<Any>) {
        super.onBind(holder, position, item, payloads)
        if (item == null) return
        onBindView(holder.binding, position, item, payloads)
    }

    protected abstract fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): VB

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}
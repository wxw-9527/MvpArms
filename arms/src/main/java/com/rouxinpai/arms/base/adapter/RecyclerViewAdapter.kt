package com.rouxinpai.arms.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:32
 * desc   :
 */

open class VbHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root)

abstract class BaseVbAdapter<VB : ViewBinding, T> : BaseQuickAdapter<T, VbHolder<VB>>(0) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VbHolder<VB> {
        val binding = onCreateViewBinding(LayoutInflater.from(parent.context), parent, viewType)
        return VbHolder(binding)
    }

    override fun convert(holder: VbHolder<VB>, item: T) {
        onBindView(holder.binding, holder.layoutPosition, item)
    }

    override fun convert(holder: VbHolder<VB>, item: T, payloads: List<Any>) {
        super.convert(holder, item, payloads)
        onBindView(holder.binding, holder.layoutPosition, item, payloads)
    }

    protected abstract fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): VB

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

/**
 *
 */
abstract class BaseVBItemProvider<T, VB : ViewBinding> : BaseItemProvider<T>() {

    override val layoutId: Int
        get() = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = onCreateViewBinding(LayoutInflater.from(parent.context), parent, viewType)
        return VbHolder(binding)
    }

    override fun convert(helper: BaseViewHolder, item: T) {
        if (helper is VbHolder<*>) {
            val binding = helper.binding as? VB ?: return
            onBindView(binding, helper.layoutPosition, item)
        }
    }

    override fun convert(helper: BaseViewHolder, item: T, payloads: List<Any>) {
        super.convert(helper, item, payloads)
        if (helper is VbHolder<*>) {
            val binding = helper.binding as? VB ?: return
            onBindView(binding, helper.layoutPosition, item, payloads)
        }
    }

    protected abstract fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): VB

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

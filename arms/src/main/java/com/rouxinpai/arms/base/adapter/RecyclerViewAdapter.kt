package com.rouxinpai.arms.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:32
 * desc   :
 */

open class VbHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root)

abstract class BaseVbAdapter<VB : ViewBinding, T>(data: MutableList<T>? = null) : BaseQuickAdapter<T, VbHolder<VB>>(0, data) {

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

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VB

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

/**
 *
 */
@Suppress("UNCHECKED_CAST")
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

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int, ): VB

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

/**
 *
 */
 @Suppress("UNCHECKED_CAST")
 abstract class BaseVbNodeProvider<T : BaseNode, VB : ViewBinding>: BaseNodeProvider() {

    override val layoutId: Int
        get() = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = onCreateViewBinding(LayoutInflater.from(context), parent, viewType)
        return VbHolder(binding)
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        if (helper is VbHolder<*>) {
            val binding = helper.binding as? VB ?: return
            val data = item as? T ?: return
            onBindView(binding, helper.layoutPosition, data)
        }
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        super.convert(helper, item, payloads)
        if (helper is VbHolder<*>) {
            val binding = helper.binding as? VB ?: return
            val data = item as? T ?: return
            onBindView(binding, helper.layoutPosition, data, payloads)
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        super.onClick(helper, view, data, position)
        if (helper is VbHolder<*>) {
            val binding = helper.binding as? VB ?: return
            val item = data as? T ?: return
            onClick(binding, view, position, item)
        }
    }

    override fun onChildClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        super.onChildClick(helper, view, data, position)
        if (helper is VbHolder<*>) {
            val binding = helper.binding as? VB ?: return
            val item = data as? T ?: return
            onChildClick(binding, view, position, item)
        }
    }

    protected abstract fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int, ): VB

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}

    protected open fun onClick(binding: VB, view: View, position: Int, item: T) {}

    protected open fun onChildClick(binding: VB, view: View, position: Int, item: T) {}
}
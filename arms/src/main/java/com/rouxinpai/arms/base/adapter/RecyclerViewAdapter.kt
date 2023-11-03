package com.rouxinpai.arms.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseDifferAdapter
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.rouxinpai.arms.util.BindingReflex

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:32
 * desc   :
 */

/**
 * ViewHolder类
 */
open class VbHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

/**
 * 基础类型 Adapter（包含点击事件、数据操作、动画、空视图）
 */
abstract class BaseVbAdapter<VB : ViewBinding, T : Any>(items: List<T> = emptyList()) :
    BaseQuickAdapter<T, VbHolder<VB>>(items) {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): VbHolder<VB> {
        val binding = BindingReflex.reflexViewBinding<VB>(
            this::class.java,
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VbHolder(binding)
    }

    override fun onBindViewHolder(holder: VbHolder<VB>, position: Int, item: T?) {
        if (item != null) {
            onBindView(holder.binding, position, item)
        }
    }

    override fun onBindViewHolder(
        holder: VbHolder<VB>,
        position: Int,
        item: T?,
        payloads: List<Any>,
    ) {
        super.onBindViewHolder(holder, position, item, payloads)
        if (item != null) {
            onBindView(holder.binding, position, item, payloads)
        }
    }

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

/**
 * 多类型布局
 */
abstract class BaseVbOnMultiItem<T : Any, VB : ViewBinding> :
    BaseMultiItemAdapter.OnMultiItem<T, VbHolder<VB>>() {

    override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): VbHolder<VB> {
        val binding = BindingReflex.reflexViewBinding<VB>(
            this::class.java,
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VbHolder(binding)
    }

    override fun onBind(holder: VbHolder<VB>, position: Int, item: T?) {
        if (item != null) {
            onBindView(holder.binding, position, item)
        }
    }

    override fun onBind(holder: VbHolder<VB>, position: Int, item: T?, payloads: List<Any>) {
        super.onBind(holder, position, item, payloads)
        if (item != null) {
            onBindView(holder.binding, position, item, payloads)
        }
    }

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

/**
 * 使用Differ方式管理数据，进行数据的局部刷新。
 */
abstract class BaseVbDifferAdapter<T : Any, VB : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>,
    items: List<T> = emptyList(),
) : BaseDifferAdapter<T, VbHolder<VB>>(diffCallback, items) {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): VbHolder<VB> {
        val binding = BindingReflex.reflexViewBinding<VB>(
            this::class.java,
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VbHolder(binding)
    }

    override fun onBindViewHolder(holder: VbHolder<VB>, position: Int, item: T?) {
        if (item != null) {
            onBindView(holder.binding, position, item)
        }
    }

    override fun onBindViewHolder(
        holder: VbHolder<VB>,
        position: Int,
        item: T?,
        payloads: List<Any>,
    ) {
        super.onBindViewHolder(holder, position, item, payloads)
        if (item != null) {
            onBindView(holder.binding, position, item, payloads)
        }
    }

    protected abstract fun onBindView(binding: VB, position: Int, item: T)

    protected open fun onBindView(binding: VB, position: Int, item: T, payloads: List<Any>) {}
}

/**
 * 只有单个item情况下的Adapter
 */
abstract class BaseVbSingleItemAdapter<T : Any, VB : ViewBinding>(item: T? = null) :
    BaseSingleItemAdapter<T, VbHolder<VB>>(item) {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): VbHolder<VB> {
        val binding = BindingReflex.reflexViewBinding<VB>(
            this::class.java,
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VbHolder(binding)
    }

    override fun onBindViewHolder(holder: VbHolder<VB>, item: T?) {
        onBindView(holder.binding, item)
    }

    override fun onBindViewHolder(holder: VbHolder<VB>, item: T?, payloads: List<Any>) {
        onBindView(holder.binding, item, payloads)
    }

    protected abstract fun onBindView(binding: VB, item: T?)

    protected open fun onBindView(binding: VB, item: T?, payloads: List<Any>) {}
}
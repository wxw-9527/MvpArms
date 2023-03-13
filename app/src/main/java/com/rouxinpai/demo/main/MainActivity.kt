package com.rouxinpai.demo.main

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.demo.databinding.ActivityMainBinding
import com.rouxinpai.demo.databinding.MainRecycleItemBinding
import com.rouxinpai.demo.model.ItemVO
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View {

    private val mAdapter = ItemAdapter()

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定适配器
        binding.rvItems.adapter = mAdapter
        // 添加分割线
        dividerBuilder()
            .asSpace()
            .size(10, TypedValue.COMPLEX_UNIT_DIP)
            .showFirstDivider()
            .showSideDividers()
            .showLastDivider()
            .build()
            .addTo(binding.rvItems)
        // 绑定监听事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItemOrNull(position) as? ItemVO ?: return@setOnItemClickListener
            item.float = 12.0f
            mAdapter.refresh(position)
        }
        binding.btnAdd.setOnClickListener {
            val item = ItemVO("", Random(120).nextInt(), Random(12).nextFloat())
            mAdapter.addData(item)
        }
    }

    private class ItemAdapter : BaseVbAdapter<MainRecycleItemBinding, ItemVO>() {

        companion object {
            // 局部刷新标志
            private const val REFRESH_TAG = 12
        }

        /**
         * 局部刷新
         */
        fun refresh(position: Int) {
            notifyItemChanged(position, REFRESH_TAG)
        }

        override fun onCreateViewBinding(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int,
        ): MainRecycleItemBinding {
            return MainRecycleItemBinding.inflate(inflater, parent, false)
        }

        override fun onBindView(binding: MainRecycleItemBinding, position: Int, item: ItemVO) {
            binding.tvStr.text = item.str
            binding.tvInt.text = item.int.toString()
            binding.tvFloat.text = item.float.toString()
            Timber.d("************ 第${position}条数据触发全局刷新")
        }

        override fun onBindView(
            binding: MainRecycleItemBinding,
            position: Int,
            item: ItemVO,
            payloads: List<Any>,
        ) {
            super.onBindView(binding, position, item, payloads)
            payloads.forEach { payload ->
                if (payload is Int && REFRESH_TAG == payload) {
                    Timber.d("============ 第${position}条数据触发局部刷新")
                    binding.tvFloat.text = item.float.toString()
                }
            }
        }
    }
}
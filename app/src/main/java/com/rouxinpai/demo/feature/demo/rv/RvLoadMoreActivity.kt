package com.rouxinpai.demo.feature.demo.rv

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.demo.databinding.RvLoadMoreActivityBinding
import com.rouxinpai.demo.databinding.RvLoadMoreRecycleItemBinding
import com.rouxinpai.demo.model.entity.demo.rv.MaterialDTO
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/6 9:59
 * desc   :
 */
@AndroidEntryPoint
class RvLoadMoreActivity :
    BaseMvpActivity<RvLoadMoreActivityBinding, RvLoadMoreContract.View, RvLoadMorePresenter>(),
    RvLoadMoreContract.View,
    OnRefreshListener,
    TrailingLoadStateAdapter.OnTrailingListener {

    // 物料列表适配器实例
    private var mMaterialAdapter = MaterialAdapter()

    override val stateLayout: View
        get() = binding.refreshLayout

    override fun onCreateAdapterHelper(): QuickAdapterHelper {
        return QuickAdapterHelper
            .Builder(mMaterialAdapter)
            .setTrailingLoadStateAdapter(this)
            .build()
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定适配器
        binding.rv.adapter = adapterHelper?.adapter
        // 添加分割线
        dividerBuilder()
            .asSpace()
            .size(8, TypedValue.COMPLEX_UNIT_DIP)
            .showFirstDivider()
            .showSideDividers()
            .showLastDivider()
            .build()
            .addTo(binding.rv)
        // 设置监听
        binding.refreshLayout.setOnRefreshListener(this)
        // 获取数据
        onRefresh()
    }

    override fun showMaterialList(list: List<MaterialDTO>) {
        binding.refreshLayout.isRefreshing = false
        mMaterialAdapter.submitList(list)
    }

    override fun showMoreMaterialList(list: List<MaterialDTO>) {
        mMaterialAdapter.addAll(list)
    }

    override fun onRefresh() {
        presenter.listMaterials()
    }

    override fun onLoad() {
        presenter.listMoreMaterials()
    }

    override fun onFailRetry() {
        presenter.listMoreMaterials()
    }

    override fun isAllowLoading(): Boolean {
        return !binding.refreshLayout.isRefreshing
    }

    /**
     * 物料列表适配器实例
     */
    private class MaterialAdapter : BaseVbAdapter<RvLoadMoreRecycleItemBinding, MaterialDTO>() {

        override fun onBindView(
            binding: RvLoadMoreRecycleItemBinding,
            position: Int,
            item: MaterialDTO,
        ) {
            binding.tvCode.text = item.materialCode
            binding.tvName.text = item.materialName
        }
    }
}
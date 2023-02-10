package com.rouxinpai.demo

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.receiver.BarcodeScanningReceiver
import com.rouxinpai.arms.view.OffsetDecoration
import com.rouxinpai.demo.databinding.ActivityMainBinding
import com.rouxinpai.demo.databinding.MainRecycleItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View,
    BarcodeScanningReceiver.OnScanListener {

    private val mAdapter = ItemAdapter()

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.rvItem.adapter = mAdapter
        dividerBuilder()
            .asSpace()
            .size(10, TypedValue.COMPLEX_UNIT_DIP)
            .showFirstDivider()
            .showSideDividers()
            .showLastDivider()
            .build()
            .addTo(binding.rvItem)
        val offsetDecoration = OffsetDecoration(120f, TypedValue.COMPLEX_UNIT_DIP)
        binding.rvItem.addItemDecoration(offsetDecoration)
        mAdapter.submitList(
            listOf(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
        )
    }

    override fun onScanned(value: String) {
        showToast(value)
    }

    private class ItemAdapter : BaseVbAdapter<MainRecycleItemBinding, String>() {

        override fun onCreateViewBinding(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ): MainRecycleItemBinding {
            return MainRecycleItemBinding.inflate(inflater, parent, false)
        }

        override fun onBindView(binding: MainRecycleItemBinding, position: Int, item: String) {

        }
    }
}
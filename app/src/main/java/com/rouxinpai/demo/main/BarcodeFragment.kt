package com.rouxinpai.demo.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.Gson
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.fragment.BaseMvpFragment
import com.rouxinpai.demo.databinding.BarcodeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/3/28 10:53
 * desc   :
 */
@AndroidEntryPoint
@EventBusEnabled
class BarcodeFragment :
    BaseMvpFragment<BarcodeFragmentBinding, BarcodeContract.View, BarcodePresenter>() {

    companion object {

        /**
         * 生成[BarcodeFragment]的实例
         */
        fun newInstance() = BarcodeFragment()
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): BarcodeFragmentBinding {
        return BarcodeFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onBarcodeEvent(event: BarcodeEvent) {
        super.onBarcodeEvent(event)
        Timber.d("------> Fragment收到条码数据")
    }

    override fun showBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
        super.showBarcodeInfo(barcodeInfo)
        binding.tv.text = Gson().toJson(barcodeInfo)
    }
}
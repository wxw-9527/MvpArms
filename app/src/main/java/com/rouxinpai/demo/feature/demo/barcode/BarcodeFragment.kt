package com.rouxinpai.demo.feature.demo.barcode

import android.os.Bundle
import androidx.core.os.bundleOf
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.fragment.BaseMvpFragment
import com.rouxinpai.demo.databinding.BarcodeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/9 10:14
 * desc   :
 */
@AndroidEntryPoint
class BarcodeFragment :
    BaseMvpFragment<BarcodeFragmentBinding, BarcodeContract.View, BarcodePresenter>(),
    BarcodeContract.View {

    companion object {

        // 参数传递标志
        private const val ARG_POSITION = "arg_position"

        /**
         * 生成[BarcodeFragment]的实例
         */
        fun newInstance(position: Int) = BarcodeFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
        }
    }

    private var mPosition: Int by Delegates.notNull()

    override fun onParseData(bundle: Bundle) {
        super.onParseData(bundle)
        mPosition = bundle.getInt(ARG_POSITION)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.tvText.text = mPosition.toString()
        binding.tvText.setOnClickListener {
            BarcodeDialog.show()
        }
    }

    override fun showBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
        dismissProgress()
    }
}
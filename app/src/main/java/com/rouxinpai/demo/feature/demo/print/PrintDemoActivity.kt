package com.rouxinpai.demo.feature.demo.print

import android.os.Bundle
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.print.SelectBarcodeActivity
import com.rouxinpai.demo.databinding.PrintDemoActivityBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/11 17:17
 * desc   :
 */
class PrintDemoActivity : BaseActivity<PrintDemoActivityBinding>() {

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 打印条码
        binding.btnPrint.setOnClickListener {
            SelectBarcodeActivity.start(
                this, listOf(
                    "10123122100000004",
                    "10124022100000019",
                    "10124022100000020",
                    "10124031400000023",
                    "10124031400000024",
                    "10123121500000002",
                    "10123121500000005",
                    "10123122100000005",
                    "10123091200000007",
                    "10123091200000008",
                    "10123091200000009",
                    "10123091200000010",
                    "10123091200000011",
                    "10123091200000012",
                    "10124022000000013",
                    "10123122000000036"
                )
            )
//            PrintActivity.start(
//                this, listOf(
//                    // 开发环境测试数据
//                    "10123122100000004",
//                    "10124022100000019",
//                    "10124022100000020",
//                    "10124031400000023",
//                    "10124031400000024",
//                    "10123121500000002",
//                    "10123121500000005",
//                    "10123122100000005",
//                    "10123091200000007",
//                    "10123091200000008",
//                    "10123091200000009",
//                    "10123091200000010",
//                    "10123091200000011",
//                    "10123091200000012",
//                    "10124022000000013",
//                    "10123122000000036",
//                    // 测试环境
////                    "10124031800000076",
////                    "BN202403512265",
//                    // 东风预生产环境测试数据
////                    "10224013000000721",
////                    "10224013000000583",
////                    "10224013000000544",
////                    "WBN202403000016",
//                    // 中科-预生产环境
////                    "10624010500000259",
////                    "10624010500000354"
//                ),
//                false
//            )
        }
    }
}
package com.rouxinpai.demo.feature.demo.print

import android.os.Bundle
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.print.PrintActivity
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
            PrintActivity.start(
                this, listOf(
                    "10123091000006117",
//                    "10123091000005164",
//                    "10123091000005949",
//                    "10123091000006388",
//                    "10123091600000017",
//                    "10123091600000016",
//                    "10123091200000004",
//                    "10123091000005164",
//                    "10123091500000091",
//                    "10123091100000014",
//                    "10123091000005646",
//                    "10123091000005636",
//                    "10123080200001228",
//                    "10123080200001386",
//                    "10123080200001751",
//                    "10123080200001241",
//                    "10123090100000020",
//                    "10123090100000019",
//                    "10123090100000018",
//                    "10123090100000017"
                )
            )
        }
    }
}
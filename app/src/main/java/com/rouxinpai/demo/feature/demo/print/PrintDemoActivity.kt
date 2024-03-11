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
                    // 开发环境测试数据
                    "10124011600000001",
//                    "10123122100000004",
//                    "10123122100000007",
//                    "10123122100000005",
//                    "10123080900000008",
                    // 东风预生产环境测试数据
//                    "10224013000000721",
//                    "10224013000000583",
//                    "10224013000000544",
                    // 中科-预生产环境
//                    "10624010500000259",
//                    "10624010500000354"
                ),
                false
            )
        }
    }
}
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
                    "10124022000000013"
                ),
                true
            )
        }
    }
}
package com.rouxinpai.demo.feature.demo.barcode

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.annotation.EventBusEnabled
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseSimpleFragmentStateAdapter
import com.rouxinpai.arms.model.bean.exception.TokenTimeoutException
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.BarcodeActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/9 10:13
 * desc   :
 */
@AndroidEntryPoint
@BarcodeScanningReceiverEnabled
@EventBusEnabled
class BarcodeActivity :
    BaseMvpActivity<BarcodeActivityBinding, BarcodeContract.View, BarcodePresenter>(),
    BarcodeContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化ViewPager
        binding.viewPager.adapter = PagerAdapter(this)
        // 绑定指示器
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, index ->
            tab.text = "页卡-$index"
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.barcode, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_dialog -> {
                BarcodeDialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBarcodeEvent(event: BarcodeEvent) {
        if (event.removeStickyEvent()) {
            presenter.getBarcodeInfo(event.barcode) {
                val errorMsg = it.message
                if (true == errorMsg?.contains("未获取到上下文信息")) {
                    dismissProgress()
                    showSuccessTip("这是个第三方编码")
                } else {
                    dismissProgress()
                    showErrorTip(errorMsg)
                    if (it is TokenTimeoutException) {
                        tokenTimeout()
                    }
                }
            }
        }
    }

    override fun showBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
        showSuccessTip(barcodeInfo.material.name)
        dismissProgress()
    }

    /**
     * ViewPager适配器
     */
    private class PagerAdapter(activity: AppCompatActivity) :
        BaseSimpleFragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            return 5
        }

        override fun createFragment(position: Int): Fragment {
            return BarcodeFragment.newInstance(position)
        }
    }
}
package com.rouxinpai.demo.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rouxinpai.arms.annotation.BarcodeScanningReceiverEnabled
import com.rouxinpai.arms.barcode.event.BarcodeEvent
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseSimpleFragmentStateAdapter
import com.rouxinpai.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
@BarcodeScanningReceiverEnabled
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View {

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.viewPager.adapter = PagerAdapter(this)
    }

    override fun onBarcodeEvent(event: BarcodeEvent) {
        super.onBarcodeEvent(event)
        Timber.d("------> Activity收到条码数据")
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        return super.onKeyMultiple(keyCode, repeatCount, event)
    }

    private class PagerAdapter(activity: AppCompatActivity) :
        BaseSimpleFragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            return 1
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> BarcodeFragment.newInstance()
                else -> throw IndexOutOfBoundsException()
            }
        }
    }
}
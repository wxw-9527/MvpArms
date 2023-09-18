package com.rouxinpai.demo.feature.demo.repeatclick

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.TimeUtils
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.constant.TimeConstants
import com.rouxinpai.arms.view.OnDebounceClickListener
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.DebounceClickDemoActivityBinding
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/3 15:32
 * desc   :
 */
class DebounceClickDemoActivity: BaseActivity<DebounceClickDemoActivityBinding>() {

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.btnClick.setOnClickListener(mOnDebounceClick)
        binding.btnClickTwo.setOnClickListener(mOnDebounceClick)
    }

    private val mOnDebounceClick = object : OnDebounceClickListener() {

        override fun onDebounceClick(v: View?) {
            when (v?.id) {
                R.id.btn_click -> {
                    Timber.d("按钮被点击 ------> ${TimeUtils.getNowString(TimeUtils.getSafeDateFormat(TimeConstants.FULL))}")
                    binding.btnClickTwo.setText("123")
                }
                R.id.btn_click_two -> {
                    Timber.d("按钮222被点击 ------> ${TimeUtils.getNowString(TimeUtils.getSafeDateFormat(TimeConstants.FULL))}")
                }
            }
        }
    }
}
package com.rouxinpai.demo.feature.demo.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import androidx.core.os.postDelayed
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.DialogActivityBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/17 13:47
 * desc   : 对话框相关示例
 */
class DialogActivity : BaseActivity<DialogActivityBinding>(), OnClickListener {

    override fun onCreateViewBinding(inflater: LayoutInflater): DialogActivityBinding {
        return DialogActivityBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定监听事件
        binding.btnProgressDialog.setOnClickListener(this)
        binding.btnSimpleDialogFragment.setOnClickListener(this)
        binding.btnSimpleBottomDialogFragment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_progress_dialog -> showProgressDialog()
            R.id.btn_simple_dialog_fragment -> showSimpleDialogFragment()
            R.id.btn_simple_bottom_dialog_fragment -> showSimpleBottomDialogFragment()
        }
    }

    // 显示进度对话框
    private fun showProgressDialog() {
        showProgress()
        Handler(Looper.getMainLooper()).postDelayed(3 * 1000L) {
            dismissProgress()
        }
    }

    // 显示简单对话框
    private fun showSimpleDialogFragment() {
        SimpleDialogFragment.show(supportFragmentManager)
    }

    // 显示简单底部对话框
    private fun showSimpleBottomDialogFragment() {
        SimpleBottomDialogFragment.show(supportFragmentManager)
    }
}
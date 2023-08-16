package com.rouxinpai.demo.feature.demo.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import androidx.core.os.postDelayed
import com.blankj.utilcode.util.TimeUtils
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.constant.TimeConstants
import com.rouxinpai.arms.dialog.DateRangeDialog
import com.rouxinpai.calendarview.Calendar
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
        binding.btnShowSuccessTip.setOnClickListener(this)
        binding.btnShowWarningTip.setOnClickListener(this)
        binding.btnShowErrorTip.setOnClickListener(this)
        binding.btnSelectDateRange.setOnClickListener(this)
        binding.btnShowSimpleCustomViewDialog.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_progress_dialog -> showProgressDialog()
            R.id.btn_simple_dialog_fragment -> showSimpleDialogFragment()
            R.id.btn_simple_bottom_dialog_fragment -> showSimpleBottomDialogFragment()
            R.id.btn_show_success_tip -> showSuccessTip()
            R.id.btn_show_warning_tip -> showWarningTip()
            R.id.btn_show_error_tip -> showErrorTip()
            R.id.btn_select_date_range -> showSelectDateRangeDialog()
            R.id.btn_show_simple_custom_view_dialog -> showSimpleCustomViewDialog()
        }
    }

    private fun showProgressDialog() {
        showProgress()
        Handler(Looper.getMainLooper()).postDelayed(3 * 1000L) {
            dismissProgress()
        }
    }

    private fun showSimpleDialogFragment() {
        SimpleDialogFragment.show(supportFragmentManager)
    }

    private fun showSimpleBottomDialogFragment() {
        SimpleBottomDialogFragment.show(supportFragmentManager)
    }

    private fun showSuccessTip() {
        showSuccessTip("这是一个成功提示")
    }

    private fun showWarningTip() {
        showWarningTip("告警提示，提示用户做了预期外的操作")
    }

    private fun showErrorTip() {
        showErrorTip("错误提示，这是一个提示用户系统发生异常")
    }

    private var mStartCalendar: Calendar? = null
    private var mEndCalendar: Calendar? = null

    private fun showSelectDateRangeDialog() {
        DateRangeDialog.show(
            mStartCalendar,
            mEndCalendar,
            object : DateRangeDialog.OnDateRangeSelectedListener {
                override fun onDateRangeSelected(startCalendar: Calendar?, endCalendar: Calendar?) {
                    mStartCalendar = startCalendar
                    mEndCalendar = endCalendar
                    if (startCalendar == null && endCalendar == null) {
                        showSuccessTip("清除选中日期范围")
                    } else {
                        val startMillis = startCalendar?.timeInMillis ?: System.currentTimeMillis()
                        val startDate = TimeUtils.millis2String(startMillis, TimeConstants.Y_MO_D)
                        val endMillis = endCalendar?.timeInMillis ?: System.currentTimeMillis()
                        val endDate = TimeUtils.millis2String(endMillis, TimeConstants.Y_MO_D)
                        showSuccessTip("$startDate 至 $endDate")
                    }
                }
            }
        )
    }

    private fun showSimpleCustomViewDialog() {
        SimpleCustomViewDialog.showBottomDialog()
    }
}
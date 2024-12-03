package com.rouxinpai.demo.feature.demo.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.OnClickListener
import androidx.core.os.postDelayed
import com.kongzue.dialogx.datepicker.CalendarDialog
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected
import com.kongzue.dialogx.datepicker.interfaces.OnMultiDateSelected
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.demo.R
import com.rouxinpai.demo.databinding.DialogActivityBinding
import timber.log.Timber
import java.util.Calendar

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/7/17 13:47
 * desc   : 对话框相关示例
 */
class DialogActivity : BaseActivity<DialogActivityBinding>(), OnClickListener {

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定监听事件
        binding.btnProgressDialog.setOnClickListener(this)
        binding.btnShowSuccessTip.setOnClickListener(this)
        binding.btnShowWarningTip.setOnClickListener(this)
        binding.btnShowErrorTip.setOnClickListener(this)
        binding.btnSelectDate.setOnClickListener(this)
        binding.btnSelectDateRange.setOnClickListener(this)
        binding.btnShowSimpleCustomViewDialog.setOnClickListener(this)
        binding.btnShowCustomViewDialog.setOnClickListener(this)
        binding.btnShowSimpleDialogFragment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_progress_dialog -> showProgressDialog()
            R.id.btn_show_success_tip -> showSuccessTip()
            R.id.btn_show_warning_tip -> showWarningTip()
            R.id.btn_show_error_tip -> showErrorTip()
            R.id.btn_select_date -> showSelectDateDialog()
            R.id.btn_select_date_range -> showSelectDateRangeDialog()
            R.id.btn_show_simple_custom_view_dialog -> showSimpleCustomViewDialog()
            R.id.btn_show_custom_view_dialog -> showCustomViewDialog()
            R.id.btn_show_simple_dialog_fragment -> showSimpleDialogFragment()
        }
    }

    private fun showProgressDialog() {
        showProgress()
        Handler(Looper.getMainLooper()).postDelayed(1 * 1000L) {
            Timber.d("进度条状态1 = ${isProgressShowing()}")
        }
        Handler(Looper.getMainLooper()).postDelayed(3 * 1000L) {
            Handler(Looper.getMainLooper()).postDelayed(1 * 1000L) {
                Timber.d("进度条状态2 = ${isProgressShowing()}")
            }
            dismissProgress()
        }
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

    private var mSelectedCalendar: Calendar? = null

    private fun showSelectDateDialog() {
        CalendarDialog.build()
            .apply {
                val calendar = mSelectedCalendar
                if (calendar != null) {
                    setDefaultSelect(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                }
            }
            .setShowLunarCalendar(true)
            .show(object : OnDateSelected() {
                override fun onSelect(text: String?, year: Int, month: Int, day: Int) {
                    mSelectedCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, day)
                    }
                }
            })
    }

    private var mStartCalendar: Calendar? = null
    private var mEndCalendar: Calendar? = null

    private fun showSelectDateRangeDialog() {
        CalendarDialog.build()
            .apply {
                val startCalendar = mStartCalendar
                val endCalendar = mEndCalendar
                if (startCalendar != null && endCalendar != null) {
                    setDefaultSelect(
                        startCalendar.get(Calendar.YEAR),
                        startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH),
                        endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH),
                    )
                }
            }
            .setMaxMultiDay(7)
            .setShowLunarCalendar(true)
            .show(object : OnMultiDateSelected() {

                override fun onSelect(startText: String?, endText: String?, startYear: Int, startMonth: Int, startDay: Int, endYear: Int, endMonth: Int, endDay: Int) {
                    mStartCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, startYear)
                        set(Calendar.MONTH, startMonth)
                        set(Calendar.DAY_OF_MONTH, startDay)
                    }
                    mEndCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, endYear)
                        set(Calendar.MONTH, endMonth)
                        set(Calendar.DAY_OF_MONTH, endDay)
                    }
                }
            })
    }

    private fun showSimpleCustomViewDialog() {
        SimpleCustomViewDialog.showBottomDialog()
    }

    private fun showCustomViewDialog() {
        CustomViewDialog.showBottomDialog()
    }

    private fun showSimpleDialogFragment() {
        SimpleDialogFragment
            .builder()
            .build()
            .show(supportFragmentManager)
    }
}
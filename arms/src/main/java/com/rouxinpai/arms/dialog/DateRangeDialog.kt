package com.rouxinpai.arms.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.fragment.BaseBottomSheetDialogFragment
import com.rouxinpai.arms.databinding.DateRangeDialogBinding
import com.rouxinpai.calendarview.Calendar
import com.rouxinpai.calendarview.CalendarView
import com.shashank.sony.fancytoastlib.FancyToast

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/12/28 14:44
 * desc   :
 */
class DateRangeDialog(
    private val startCalendar: Calendar? = null,
    private val endCalendar: Calendar? = null
) : BaseBottomSheetDialogFragment<DateRangeDialogBinding>(),
    CalendarView.OnMonthChangeListener,
    OnClickListener {

    companion object {

        /**
         * 展示
         */
        fun show(manager: FragmentManager, startCalendar: Calendar?, endCalendar: Calendar?) {
            val dialogFragment = DateRangeDialog(startCalendar, endCalendar)
            dialogFragment.show(manager, DateRangeDialog::class.java.simpleName)
        }
    }

    private lateinit var mOnDateRangeSelectedListener: OnDateRangeSelectedListener

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DateRangeDialogBinding {
        return DateRangeDialogBinding.inflate(inflater, parent, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDateRangeSelectedListener = context as OnDateRangeSelectedListener
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定监听事件
        binding.calendarView.setOnMonthChangeListener(this)
        binding.btnClear.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        // 渲染界面
        binding.tvYearMonth.text = getString(
            R.string.date_range__year_month,
            binding.calendarView.curYear,
            binding.calendarView.curMonth
        )
        binding.calendarView.setRange(
            2020, 1, 1,
            binding.calendarView.curYear, binding.calendarView.curMonth, binding.calendarView.curDay
        )
        if (startCalendar != null && endCalendar != null) {
            binding.calendarView.setSelectStartCalendar(startCalendar)
            binding.calendarView.setSelectEndCalendar(endCalendar)
            binding.calendarView.post {
                binding.calendarView.scrollToSelectCalendar()
            }
        } else {
            binding.calendarView.post {
                binding.calendarView.scrollToCurrent()
            }
        }
    }

    override fun onMonthChange(year: Int, month: Int) {
        binding.tvYearMonth.text = getString(R.string.date_range__year_month, year, month)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_clear -> onClearClick()
            R.id.btn_cancel -> onCancelClick()
            R.id.btn_confirm -> onConfirmClick()
        }
    }

    /**
     * 清除选中按钮点击事件
     */
    private fun onClearClick() {
        mOnDateRangeSelectedListener.onDateRangeSelected(null, null)
        dismiss()
    }

    /**
     * 取消按钮点击事件
     */
    private fun onCancelClick() {
        dismiss()
    }

    /**
     * 确认按钮点击事件
     */
    private fun onConfirmClick() {
        val list = binding.calendarView.selectCalendarRange
        if (list.isNullOrEmpty()) {
            showWarningToast(R.string.date_range__please_select_the_correct_start_date)
        } else {
            mOnDateRangeSelectedListener.onDateRangeSelected(list.first(), list.last())
            dismiss()
        }
    }

    /**
     * 提示异常信息
     */
    private fun showWarningToast(messageId: Int) {
        val message = getString(messageId)
        val duration = FancyToast.LENGTH_SHORT
        val type = FancyToast.WARNING
        FancyToast.makeText(requireContext(), message, duration, type, false).show()
    }

    interface OnDateRangeSelectedListener {
        fun onDateRangeSelected(startCalendar: Calendar?, endCalendar: Calendar?)
    }
}
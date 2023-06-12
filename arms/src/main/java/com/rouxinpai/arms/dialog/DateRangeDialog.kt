@file:Suppress("DEPRECATION")

package com.rouxinpai.arms.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
class DateRangeDialog : BaseBottomSheetDialogFragment<DateRangeDialogBinding>(),
    CalendarView.OnMonthChangeListener,
    OnClickListener {

    companion object {

        // 参数传递标志
        private const val ARG_START_CALENDAR = "arg_start_calendar" // 开始日期
        private const val ARG_END_CALENDAR = "arg_end_calendar" // 结束日期

        /**
         * 展示
         */
        fun show(
            manager: FragmentManager,
            startCalendar: Calendar? = null,
            endCalendar: Calendar? = null,
            listener: OnDateRangeSelectedListener? = null
        ) {
            val dialogFragment = DateRangeDialog().apply {
                arguments = bundleOf(
                    ARG_START_CALENDAR to startCalendar,
                    ARG_END_CALENDAR to endCalendar
                )
                if (listener != null) {
                    setOnDateRangeSelectedListener(listener)
                }
            }
            dialogFragment.show(manager, DateRangeDialog::class.java.simpleName)
        }
    }

    // 开始日期
    private var mStartCalendar: Calendar? = null

    // 结束日期
    private var mEndCalendar: Calendar? = null

    // 监听事件
    private var mOnDateRangeSelectedListener: OnDateRangeSelectedListener? = null

    override fun onParseData(bundle: Bundle) {
        super.onParseData(bundle)
        mStartCalendar = bundle.getSerializable(ARG_START_CALENDAR) as? Calendar
        mEndCalendar = bundle.getSerializable(ARG_END_CALENDAR) as? Calendar
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DateRangeDialogBinding {
        return DateRangeDialogBinding.inflate(inflater, parent, false)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定监听事件
        binding.calendarView.setOnMonthChangeListener(this)
        binding.btnClear.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        // 初始化界面
        val year = binding.calendarView.curYear
        val month = binding.calendarView.curMonth
        binding.tvYearMonth.text = getString(R.string.date_range__year_month, year, month)
        if (mStartCalendar != null && mEndCalendar != null) {
            binding.calendarView.setSelectStartCalendar(mStartCalendar)
            binding.calendarView.setSelectEndCalendar(mEndCalendar)
            binding.calendarView.post {
                binding.calendarView.scrollToSelectCalendar()
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
        mOnDateRangeSelectedListener?.onDateRangeSelected(null, null)
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
            showToast(
                R.string.date_range__please_select_the_correct_start_date,
                type = FancyToast.WARNING
            )
        } else {
            val startCalendar = list.first()
            val endCalendar = list.last()
            mOnDateRangeSelectedListener?.onDateRangeSelected(startCalendar, endCalendar)
            dismiss()
        }
    }

    /**
     * 绑定监听事件
     */
    fun setOnDateRangeSelectedListener(listener: OnDateRangeSelectedListener) {
        mOnDateRangeSelectedListener = listener
    }

    interface OnDateRangeSelectedListener {
        fun onDateRangeSelected(startCalendar: Calendar?, endCalendar: Calendar?)
    }
}
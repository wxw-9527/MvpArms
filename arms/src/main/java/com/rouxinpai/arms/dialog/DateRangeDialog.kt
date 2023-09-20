package com.rouxinpai.arms.dialog

import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.LifecycleOwner
import com.kongzue.dialogx.dialogs.BottomDialog
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.dialog.BaseOnBindView
import com.rouxinpai.arms.databinding.DateRangeDialogBinding
import com.rouxinpai.calendarview.Calendar
import com.rouxinpai.calendarview.CalendarView

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/12/28 14:44
 * desc   :
 */
class DateRangeDialog :
    BaseOnBindView<BottomDialog, DateRangeDialogBinding>(R.layout.date_range_dialog),
    CalendarView.OnMonthChangeListener,
    OnClickListener {

    companion object {

        /**
         * 展示
         */
        fun show(
            startCalendar: Calendar? = null,
            endCalendar: Calendar? = null,
            listener: OnDateRangeSelectedListener? = null,
        ) {
            val customView = DateRangeDialog().apply {
                setStartCalendar(startCalendar)
                setEndCalendar(endCalendar)
                setOnDateRangeSelectedListener(listener)
            }
            BottomDialog
                .build()
                .setAllowInterceptTouch(false)
                .setCustomView(customView)
                .show()
        }
    }

    // 开始日期
    private var mStartCalendar: Calendar? = null

    // 结束日期
    private var mEndCalendar: Calendar? = null

    // 监听事件
    private var mOnDateRangeSelectedListener: OnDateRangeSelectedListener? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // 初始化界面
        val year = binding.calendarView.curYear
        val month = binding.calendarView.curMonth
        binding.tvYearMonth.text = context.getString(R.string.date_range__year_month, year, month)
        if (mStartCalendar != null && mEndCalendar != null) {
            binding.calendarView.setSelectStartCalendar(mStartCalendar)
            binding.calendarView.setSelectEndCalendar(mEndCalendar)
            binding.calendarView.post {
                binding.calendarView.scrollToSelectCalendar()
            }
        }
        // 绑定监听事件
        binding.calendarView.setOnMonthChangeListener(this)
        binding.btnClear.setOnClickListener(this)
        dialog.setCancelButton(R.string.date_range__cancel)
            .setOkButton(R.string.date_range__ok) { _, _ -> onConfirmClick() }
    }

    override fun onMonthChange(year: Int, month: Int) {
        binding.tvYearMonth.text = context.getString(R.string.date_range__year_month, year, month)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_clear -> onClearClick()
        }
    }

    /**
     * 清除选中按钮点击事件
     */
    private fun onClearClick() {
        mOnDateRangeSelectedListener?.onDateRangeSelected(null, null)
        dialog.dismiss()
    }

    /**
     * 确认按钮点击事件
     */
    private fun onConfirmClick(): Boolean {
        val list = binding.calendarView.selectCalendarRange
        return if (list.isNullOrEmpty()) {
            showWarningTip(R.string.date_range__please_select_the_correct_start_date)
            true
        } else {
            val startCalendar = list.first()
            val endCalendar = list.last()
            mOnDateRangeSelectedListener?.onDateRangeSelected(startCalendar, endCalendar)
            false
        }
    }

    /**
     *
     */
    fun setStartCalendar(calendar: Calendar?) {
        mStartCalendar = calendar
    }

    /**
     *
     */
    fun setEndCalendar(calendar: Calendar?) {
        mEndCalendar = calendar
    }

    /**
     * 绑定监听事件
     */
    fun setOnDateRangeSelectedListener(listener: OnDateRangeSelectedListener?) {
        mOnDateRangeSelectedListener = listener
    }

    interface OnDateRangeSelectedListener {
        fun onDateRangeSelected(startCalendar: Calendar?, endCalendar: Calendar?)
    }
}
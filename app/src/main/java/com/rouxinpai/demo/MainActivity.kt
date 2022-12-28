package com.rouxinpai.demo

import android.os.Bundle
import android.view.LayoutInflater
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.receiver.BarcodeScanningReceiver
import com.rouxinpai.arms.dialog.DateRangeDialog
import com.rouxinpai.calendarview.Calendar
import com.rouxinpai.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View,
    BarcodeScanningReceiver.OnScanListener,
    DateRangeDialog.OnDateRangeSelectedListener {

    private var mStartCalendar: Calendar? = null
    private var mEndCalendar: Calendar? = null

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        //
        binding.btnShow.setOnClickListener {
            DateRangeDialog.show(supportFragmentManager, mStartCalendar, mEndCalendar)
        }
    }

    override fun onScanned(value: String) {
        showToast(value)
    }

    override fun onDateRangeSelected(startCalendar: Calendar?, endCalendar: Calendar?) {
        mStartCalendar = startCalendar
        mEndCalendar = endCalendar
    }
}
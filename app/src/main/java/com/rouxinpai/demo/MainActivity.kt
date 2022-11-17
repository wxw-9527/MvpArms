package com.rouxinpai.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View {

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        findViewById<TextView>(R.id.tv).setOnClickListener {
            // showProgress()
            presenter.print("Hello World!")
        }
    }
}
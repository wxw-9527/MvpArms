package com.rouxinpai.arms.di

import com.rouxinpai.arms.print.PrintConfigPresenter
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/3 22:58
 * desc   :
 */
@EntryPoint
@InstallIn(ActivityComponent::class)
interface DialogPresenterEntryPoint {

    fun printConfigPresenter(): PrintConfigPresenter
}

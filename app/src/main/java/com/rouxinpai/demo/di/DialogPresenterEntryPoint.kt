package com.rouxinpai.demo.di

import com.rouxinpai.demo.feature.demo.barcode.BarcodePresenter
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/17 15:41
 * desc   :
 */
@EntryPoint
@InstallIn(ActivityComponent::class)
interface DialogPresenterEntryPoint {

   fun barcodePresenter(): BarcodePresenter
}
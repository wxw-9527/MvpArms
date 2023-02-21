package com.rouxinpai.demo.main

import android.os.Bundle
import android.view.LayoutInflater
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.extension.NineGridView
import com.rouxinpai.arms.extension.takePhoto
import com.rouxinpai.arms.receiver.BarcodeScanningReceiver
import com.rouxinpai.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseMvpActivity<ActivityMainBinding, MainContract.View, MainPresenter>(),
    MainContract.View,
    BarcodeScanningReceiver.OnScanListener,
    NineGridView.OnAddClickListener {

    override fun onCreateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        binding.nineGridView.setOnAddClickListener(this)
    }

    override fun onScanned(barcode: String) {
        showToast(barcode)
    }

    private var mSelected = emptyList<LocalMedia>()

    override fun onAddClick() {
//        PictureSelector
//            .create(this)
//            .selectPicture(5, mSelected) { list ->
//                mSelected = list
//                binding.nineGridView.addData(list.map { it.availablePath })
//            }
        PictureSelector
            .create(this)
            .takePhoto {
                binding.nineGridView.addData(it.availablePath)
            }
    }
}
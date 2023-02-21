package com.rouxinpai.demo.main

import android.os.Bundle
import android.view.LayoutInflater
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.extension.GlideEngine
import com.rouxinpai.arms.extension.LuBanCompressEngine
import com.rouxinpai.arms.extension.NineGridView
import com.rouxinpai.arms.receiver.BarcodeScanningReceiver
import com.rouxinpai.demo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

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

fun PictureSelector.selectPicture(
    maxSelectNum: Int,
    selectedList: List<LocalMedia>? = null,
    block: (List<LocalMedia>) -> Unit
) {
    this.openGallery(SelectMimeType.ofImage())
        .setImageEngine(GlideEngine.instance)
        .setCompressEngine(LuBanCompressEngine.instance)
        .isMaxSelectEnabledMask(true)
        .setMaxSelectNum(maxSelectNum) // 图片最大选择数量
        .setSelectedData(selectedList)
        .forResult(object : OnResultCallbackListener<LocalMedia> {

            override fun onResult(result: ArrayList<LocalMedia>) {
                block.invoke(result)
            }

            override fun onCancel() {

            }
        })
}

fun PictureSelector.takePhoto(
    block: (LocalMedia) -> Unit
) {
    this.openCamera(SelectMimeType.ofImage())
        .setCompressEngine(LuBanCompressEngine.instance)
        .forResult(object : OnResultCallbackListener<LocalMedia> {

            override fun onResult(result: ArrayList<LocalMedia>) {
                block.invoke(result.first())
            }

            override fun onCancel() {

            }
        })
}
package com.rouxinpai.demo.feature.demo.pictureselector

import android.os.Bundle
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.extension.takePhoto
import com.rouxinpai.demo.databinding.PictureSelectorDemoActivityBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/1/24 14:34
 * desc   :
 */
class PictureSelectorDemoActivity: BaseActivity<PictureSelectorDemoActivityBinding>() {

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 拍照并压缩
        binding.btnTakeCompress.setOnClickListener {
            PictureSelector.create(this)
                .takePhoto {
                    Glide.with(this).load(it.availablePath).into(binding.ivImage)
                }
        }
    }
}
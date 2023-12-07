package com.rouxinpai.demo.feature.demo.openimage

import android.os.Bundle
import com.bumptech.glide.Glide
import com.rouxinpai.arms.base.activity.BaseActivity
import com.rouxinpai.arms.extension.show
import com.rouxinpai.arms.view.NineGridView
import com.rouxinpai.demo.databinding.OpenImageActivityBinding

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/12/7 11:49
 * desc   :
 */
class OpenImageActivity: BaseActivity<OpenImageActivityBinding>() {

    private val mImageUrlList = listOf(
        "https://img0.baidu.com/it/u=3609287531,1132265136&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=728",
        "https://img2.baidu.com/it/u=1717326383,302483114&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=738",
        "https://img2.baidu.com/it/u=2291218047,1355195757&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=726",
        "https://img2.baidu.com/it/u=903535653,2041127539&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750",
    )

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)

        Glide.with(this).load(mImageUrlList.first()).into(binding.imageView)

        binding.imageView.setOnClickListener {
            binding.imageView.show(mImageUrlList.first())
        }

        binding.ngvPhoto.setNewList(mImageUrlList.map { ImageEntity(it) })
    }
}

data class ImageEntity(val url: String): NineGridView.IEntity {

    override val path: String
        get() = url
}
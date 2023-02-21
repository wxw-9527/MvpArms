package com.rouxinpai.arms.extension

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.utils.ActivityCompatHelper
import com.luck.picture.lib.R
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File
import java.util.ArrayList


/**
 *
 */
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

/**
 *
 */
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

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/21 10:13
 * desc   :
 */
class GlideEngine private constructor() : ImageEngine {

    companion object {
        /**
         * 返回[GlideEngine]单例对象
         */
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { GlideEngine() }
    }

    /**
     * 加载图片
     *
     * @param context   上下文
     * @param url       资源url
     * @param imageView 图片承载控件
     */
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    override fun loadImage(
        context: Context,
        imageView: ImageView,
        url: String,
        maxWidth: Int,
        maxHeight: Int
    ) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .load(url)
            .override(maxWidth, maxHeight)
            .into(imageView)
    }

    /**
     * 加载相册目录封面
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .sizeMultiplier(0.5f)
            .transform(CenterCrop(), RoundedCorners(8))
            .placeholder(R.drawable.ps_ic_placeholder)
            .into(imageView)
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .placeholder(R.drawable.ps_ic_placeholder)
            .into(imageView)
    }

    override fun pauseRequests(context: Context) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context).pauseRequests()
    }

    override fun resumeRequests(context: Context) {
        if (!ActivityCompatHelper.assertValidRequest(context)) {
            return
        }
        Glide.with(context).resumeRequests()
    }
}

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/2/21 10:13
 * desc   :
 */
class LuBanCompressEngine : CompressFileEngine {

    companion object {
        /**
         * 返回[GlideEngine]单例对象
         */
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LuBanCompressEngine() }
    }

    override fun onStartCompress(
        context: Context,
        source: ArrayList<Uri>,
        call: OnKeyValueResultCallbackListener?
    ) {
        Luban.with(context)
            .load(source)
            .ignoreBy(100)
            .setCompressListener(object : OnNewCompressListener {

                override fun onStart() {

                }

                override fun onSuccess(source: String?, compressFile: File) {
                    call?.onCallback(source, compressFile.absolutePath)
                }

                override fun onError(source: String?, e: Throwable?) {
                    call?.onCallback(source, null)
                }
            })
            .launch()
    }
}
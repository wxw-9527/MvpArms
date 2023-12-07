package com.rouxinpai.arms.extension

import android.widget.GridView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.flyjingfish.openimagelib.OpenImage
import com.flyjingfish.openimagelib.enums.MediaType

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/12/7 11:39
 * desc   :
 */
fun ImageView.show(url: String) {
    OpenImage.with(context)
        .setClickImageView(this)
        .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP, true)
        .setImageUrl(url, MediaType.IMAGE)
        .show()
}

fun GridView.show(id: Int, urlList: List<String>, index: Int) {
    OpenImage.with(context)
        .setClickGridView(this) { _, _ -> id }
        .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP, true)
        .setImageUrlList(urlList, MediaType.IMAGE)
        .setClickPosition(index)
        .show()
}

fun RecyclerView.show(id: Int, urlList: List<String>, index: Int) {
    OpenImage.with(context)
        .setClickRecyclerView(this) { _, _ -> id }
        .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP, true)
        .setImageUrlList(urlList, MediaType.IMAGE)
        .setClickPosition(index)
        .show()
}
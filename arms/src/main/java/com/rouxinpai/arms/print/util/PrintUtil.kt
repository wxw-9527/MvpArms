package com.rouxinpai.arms.print.util

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import com.rouxinpai.arms.print.model.BrandEnum
import com.rouxinpai.arms.print.model.TemplateVO
import com.tencent.mmkv.MMKV

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/19 14:07
 * desc   :
 */
object PrintUtil {

    //
    private val mDefaultMMKV: MMKV by lazy(LazyThreadSafetyMode.NONE) {
        MMKV.defaultMMKV()
    }

    // 常量，用于存储和获取打印机品牌对象的建
    private const val KEY_BRAND = "key_printer_brand"

    // 保存打印机品牌对象的实例变量
    private var mBrandEnum: BrandEnum? = null

    /**
     * 保存打印机品牌信息
     */
    fun setBrandEnum(brandEnum: BrandEnum) {
        mBrandEnum = brandEnum
        mDefaultMMKV.encode(KEY_BRAND, brandEnum.brand)
    }

    /**
     * 获取已保存的打印机品牌
     */
    fun getBrandEnum(): BrandEnum {
        val brand = mDefaultMMKV.decodeString(KEY_BRAND)
        return BrandEnum.fromBrand(brand)
    }

    // 常量，用于存储和获取 TemplateVO 对象的键
    private const val KEY_TEMPLATE = "key_template"

    // 保存 TemplateVO 对象的实例变量
    private var mTemplateVO: TemplateVO? = null

    /**
     * 保存打印模板信息
     */
    fun setTemplate(template: TemplateVO) {
        mTemplateVO = template
        mDefaultMMKV.encode(KEY_TEMPLATE, template)
    }

    /**
     * 获取已缓存的打印模板
     */
    fun getTemplate(): TemplateVO? {
        if (mTemplateVO == null) {
            mTemplateVO = mDefaultMMKV.decodeParcelable(KEY_TEMPLATE, TemplateVO::class.java)
        }
        return mTemplateVO
    }

    /**
     * 清除全部数据
     */
    fun clearAll() {
        // 清除 TemplateVO 对象
        mTemplateVO = null
        // 清除 MMKV
        mDefaultMMKV.clearAll()
    }

    /**
     * 将彩色位图转换为二值黑白图像。
     *
     * @param bm 要转换的原始位图
     * @return 转换后的二值黑白位图
     */
    fun getBinationalBitmap(bm: Bitmap): Bitmap {
        // 复制原始位图，确保操作不会改变原始位图
        val bitmap = bm.copy(Bitmap.Config.ARGB_8888, true)
        val width = bm.width
        val height = bm.height
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        // 遍历每个像素点
        for (i in 0 until width) {
            for (j in 0 until height) {
                // 获取当前像素点的颜色值
                val pixel = bitmap.getPixel(i, j)
                val alpha = pixel and 0xFF000000.toInt()
                val red = (pixel and 0x00FF0000) shr 16
                val green = (pixel and 0x0000FF00) shr 8
                val blue = pixel and 0x000000FF
                // 根据计算公式将彩色像素转换为灰度值
                var gray = (red * 0.3 + green * 0.59 + blue * 0.11).toInt()
                // 根据阈值将灰度值二值化为黑白
                gray = if (gray <= 95) 0 else 255
                // 构造新的像素值，包含透明度和黑白值
                val newPixel = alpha or (gray shl 16) or (gray shl 8) or gray
                // 在位图中设置新的像素值
                bitmap.setPixel(i, j, newPixel)
            }
        }
        // 返回处理后的位图
        return bitmap
    }
}
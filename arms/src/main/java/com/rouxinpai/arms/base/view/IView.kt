package com.rouxinpai.arms.base.view

import androidx.annotation.StringRes
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.update.model.UpdateInfo
import com.shashank.sony.fancytoastlib.FancyToast
import com.shashank.sony.fancytoastlib.FancyToast.Duration
import com.shashank.sony.fancytoastlib.FancyToast.LayoutType

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/16 9:51
 * desc   :
 */
interface IView: ILoadMore {

    /**
     * 显示 Toast 提示
     *
     * @param messageId 提示文本资源 ID
     * @param duration  显示时间长度，默认为 LENGTH_SHORT
     * @param type      提示样式，默认为 DEFAULT
     */
    fun showToast(
        @StringRes messageId: Int,
        @Duration duration: Int = FancyToast.LENGTH_SHORT,
        @LayoutType type: Int = FancyToast.DEFAULT
    )

    /**
     * 显示 Toast 提示
     *
     * @param message   提示文本
     * @param duration  显示时间长度，默认为 LENGTH_SHORT
     * @param type      提示样式，默认为 DEFAULT
     */
    fun showToast(
        message: CharSequence?,
        @Duration duration: Int = FancyToast.LENGTH_SHORT,
        @LayoutType type: Int = FancyToast.DEFAULT
    )

    /**
     * 显示加载进度弹窗
     *
     * @param labelId   进度条消息文本资源 ID
     * @param detailId  进度条详情文本资源 ID
     */
    fun showProgress(@StringRes labelId: Int, @StringRes detailId: Int? = null)

    /**
     * 显示加载进度弹窗
     *
     * @param labelMsg  进度条消息文本
     * @param detailMsg 进度条详情文本
     */
    fun showProgress(labelMsg: CharSequence? = null, detailMsg: String? = null)

    /**
     * 更新进度弹窗的详情文本
     *
     * @param detailId  进度条详情文本资源 ID
     */
    fun updateProgress(@StringRes detailId: Int)

    /**
     * 更新进度弹窗的详情文本
     *
     * @param detailMsg 进度条详情文本
     */
    fun updateProgress(detailMsg: String? = null)

    /**
     * 销毁加载进度弹窗
     */
    fun dismissProgress()

    /**
     * 展示加载中状状态
     *
     * @param msgId 加载中消息文本资源 ID
     * @param msg   加载其他消息文本
     * @param descId 加载中描述文本资源 ID
     * @param desc  加载其他描述文本
     */
    fun showLoadingPage(
        @StringRes msgId: Int? = null,
        msg: String? = null,
        @StringRes descId: Int? = null,
        desc: String? = null
    )

    /**
     * 展示空数据状状态
     *
     * @param msgId 空数据消息文本资源 ID
     * @param msg   空数据其他消息文本
     */
    fun showEmptyPage(@StringRes msgId: Int? = null, msg: String? = null)

    /**
     * 展示加载异常状状态
     *
     * @param msgId 加载异常消息文本资源 ID
     * @param msg   加载异常其他消息文本
     * @param descId 加载异常描述文本资源 ID
     * @param desc  加载异常其他描述文本
     */
    fun showErrorPage(
        @StringRes msgId: Int? = null,
        msg: String? = null,
        @StringRes descId: Int? = null,
        desc: String? = null
    )

    /**
     * 展示加载成功状态
     */
    fun showSuccessPage()

    /**
     * 显示条码信息
     *
     * @param barcodeInfo 条码信息对象
     */
    fun showBarcodeInfo(barcodeInfo: BarcodeInfoVO) {}

    /**
     * 显示版本更新对话框
     *
     * @param updateInfo 版本更新信息对象
     */
    fun showUpdateInfo(updateInfo: UpdateInfo) {}

    /**
     * token超时
     */
    fun tokenTimeout()
}
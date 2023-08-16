package com.rouxinpai.arms.base.view

import androidx.annotation.StringRes
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.update.model.UpdateInfo

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/16 9:51
 * desc   :
 */
interface IView: ILoadMore {

    /**
     * 显示简单提示
     *
     * @param messageId 提示文本资源 ID
     */
    fun showSuccessTip(@StringRes messageId: Int)

    /**
     * 显示简单提示
     *
     * @param message 提示文本
     */
    fun showSuccessTip(message: CharSequence?)

    /**
     * 显示告警提示
     *
     * @param messageId 提示文本资源 ID
     */
    fun showWarningTip(@StringRes messageId: Int)

    /**
     * 显示告警提示
     *
     * @param message 提示文本
     */
    fun showWarningTip(message: CharSequence?)

    /**
     * 显示错误提示
     *
     * @param messageId 提示文本资源 ID
     */
    fun showErrorTip(@StringRes messageId: Int)

    /**
     * 显示错误提示
     *
     * @param message 提示文本
     */
    fun showErrorTip(message: CharSequence?)

    /**
     * 显示加载进度弹窗
     *
     * @param messageId   进度条消息文本资源 ID
     */
    fun showProgress(@StringRes messageId: Int?)

    /**
     * 显示加载进度弹窗
     *
     * @param message  进度条消息文本
     */
    fun showProgress(message: CharSequence? = null)

    /**
     * 销毁加载进度弹窗
     */
    fun dismissProgress()

    /**
     * 判断加载进度弹窗是否正在展示
     */
    fun isProgressShowing(): Boolean

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
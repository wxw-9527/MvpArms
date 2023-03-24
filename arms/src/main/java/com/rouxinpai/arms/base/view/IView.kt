package com.rouxinpai.arms.base.view

import androidx.annotation.StringRes
import com.shashank.sony.fancytoastlib.FancyToast
import com.shashank.sony.fancytoastlib.FancyToast.Duration
import com.shashank.sony.fancytoastlib.FancyToast.LayoutType

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/16 9:51
 * desc   :
 */
interface IView {

    /**
     * 弹出吐司提示
     * @param messageId
     * @param duration
     * @param type
     */
    fun showToast(@StringRes messageId: Int, @Duration duration: Int = FancyToast.LENGTH_SHORT, @LayoutType type: Int = FancyToast.DEFAULT)

    /**
     * 弹出吐司提示
     * @param message
     * @param duration
     * @param type
     */
    fun showToast(message: CharSequence?, @Duration duration: Int = FancyToast.LENGTH_SHORT, @LayoutType type: Int = FancyToast.DEFAULT)

    /**
     * 弹出加载进度弹窗
     * @param labelId
     */
    fun showProgress(@StringRes labelId: Int, @StringRes detailId: Int? = null)

    /**
     * 弹出加载进度弹窗
     * @param labelMsg
     */
    fun showProgress(labelMsg: CharSequence? = null, detailMsg: String? = null)

    /**
     * 更新进度弹窗内容
     */
    fun updateProgress(@StringRes detailId: Int)

    /**
     * 更新进度弹窗内容
     */
    fun updateProgress(detailMsg: String? = null)

    /**
     * 销毁加载进度弹窗
     */
    fun dismiss()

    /**
     * 展示加载中状态
     */
    fun showLoadingPage(@StringRes msgId: Int? = null, msg: String? = null, @StringRes descId: Int? = null, desc: String? = null)

    /**
     * 展示空数据状态
     */
    fun showEmptyPage(@StringRes msgId: Int? = null, msg: String? = null)

    /**
     * 展示加载异常状态
     */
    fun showErrorPage(@StringRes msgId: Int? = null, msg: String? = null, @StringRes descId: Int? = null, desc: String? = null)

    /**
     * 展示加载成功状态
     */
    fun showSuccessPage()

    /**
     * 本次数据加载完毕
     */
    fun loadMoreComplete()

    /**
     * 所有数据加载完成
     */
    fun loadMoreEnd(gone: Boolean = false)

    /**
     * 本次数据加载错误
     */
    fun loadMoreFail()

    /**
     * token超时
     */
    fun tokenTimeout()
}
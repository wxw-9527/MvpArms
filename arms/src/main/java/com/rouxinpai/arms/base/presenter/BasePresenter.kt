package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import com.google.gson.JsonObject
import com.rouxinpai.arms.barcode.api.BarcodeApi
import com.rouxinpai.arms.barcode.model.BarcodeInfoDTO
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.request
import com.rouxinpai.arms.update.api.UpdateApi
import com.rouxinpai.arms.update.model.UpdateInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:53
 * desc   :
 */
abstract class BasePresenter<V : IView> : IPresenter<V>,
    CoroutineScope by CoroutineScope(Job() + Dispatchers.Main) {

    @Inject
    lateinit var retrofit: Retrofit

    private var mLifecycle: Lifecycle? = null

    var view: V? = null
        private set

    override fun bind(lifecycle: Lifecycle, v: V) {
        this.mLifecycle = lifecycle
        this.view = v
        this.mLifecycle?.addObserver(this)
    }

    override fun getBarcodeInfo(barcode: String) {
        request<BarcodeInfoDTO> {
            donotShowErrorPage()
            start {
                view?.showProgress()
            }
            call {
                val jsonObject = JsonObject().apply { addProperty("barCode", barcode) }
                val body = jsonObject.toRequestBody()
                retrofit.create<BarcodeApi>().getBarcodeInfo(body)
            }
            success { _, barcodeInfoDto ->
                val barcodeInfo = BarcodeInfoVO.convertFromDTO(barcodeInfoDto)
                view?.dismiss()
                handleBarcodeInfo(barcodeInfo)
            }
        }
    }

    override fun getUpdateInfo(clientType: String, clientName: String, channel: String) {
        request<UpdateInfo> {
            skipDefaultErrorLogic()
            donotShowErrorPage()
            call {
                retrofit.create<UpdateApi>().getUpdateInfo(clientType, clientName)
            }
            success { _, updateInfo ->
                val versionCode = AppUtils.getAppVersionCode()
                if (channel == updateInfo.channelFlag && versionCode < updateInfo.clientVersionCode) {
                    if (updateInfo.sysClientFiles.isNotEmpty()) {
                        view?.showUpdateInfo(updateInfo)
                    }
                }
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cancel()
        mLifecycle?.removeObserver(this)
        mLifecycle = null
        view = null
        super.onDestroy(owner)
    }

    /**
     * 处理条码上下文数据
     */
    open fun handleBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
        view?.showBarcodeInfo(barcodeInfo)
    }
}
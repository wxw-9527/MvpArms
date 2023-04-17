package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.barcode.api.BarcodeApi
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.BaseSubscriber
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.update.api.UpdateApi
import com.rouxinpai.arms.update.model.UpdateInfo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 14:53
 * desc   :
 */
abstract class BasePresenter<V : IView> : IPresenter<V> {

    @Inject
    lateinit var retrofit: Retrofit

    private var mLifecycle: Lifecycle? = null

    private val mDisposable = CompositeDisposable()

    // 有条码未被消费
    private var mHasUnconsumedBarcode = false

    var view: V? = null
        private set

    override fun bind(lifecycle: Lifecycle, v: V) {
        this.mLifecycle = lifecycle
        this.view = v
        this.mLifecycle?.addObserver(this)
    }

    override fun addDisposable(disposable: Disposable) {
        mDisposable.add(disposable)
    }

    override fun getBarcodeInfo(barcode: String) {
        if (mHasUnconsumedBarcode) {
            Timber.d("------> 有条码未被消费，请勿重复扫描")
            return
        }
        view?.showProgress()
        mHasUnconsumedBarcode = true
        val jsonObject = JsonObject().apply {
            addProperty("barCode", barcode)
            add("billTypes", JsonArray().apply { add("quantity") })
        }
        val body = jsonObject.toRequestBody()
        val disposable = retrofit.create<BarcodeApi>().getBarcodeInfo(body)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .map { BarcodeInfoVO.convertFromDTO(it) }
            .subscribeWith(object : BaseSubscriber<BarcodeInfoVO>(view, false) {

                override fun onData(t: BarcodeInfoVO) {
                    view?.dismiss()
                    handleBarcodeInfo(t)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    consumeBarcode()
                }
            })
        addDisposable(disposable)
    }

    override fun consumeBarcode() {
        mHasUnconsumedBarcode = false
    }

    override fun getUpdateInfo(clientType: String, clientName: String, channel: String) {
        val disposable = retrofit.create<UpdateApi>().getUpdateInfo(clientType, clientName)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .subscribeWith(object : BaseSubscriber<UpdateInfo>(view, false) {

                override fun onData(t: UpdateInfo) {
                    handleUpgradeInfo(channel, t)
                }
            })
        addDisposable(disposable)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mDisposable.clear()
        mLifecycle?.removeObserver(this)
        mLifecycle = null
        view = null
        super.onDestroy(owner)
    }

    /**
     * 处理条码上下文数据
     */
    open fun handleBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
        // 如果条码不是物料条码，则消费条码
        if (!barcodeInfo.isMaterialBarcode) {
            consumeBarcode()
        }
        // 显示条码信息
        view?.showBarcodeInfo(barcodeInfo)
    }

    /**
     * 处理版本更新数据
     */
    open fun handleUpgradeInfo(channel: String, updateInfo: UpdateInfo) {
        Timber.d("------> 当前渠道号：${channel}")
        val versionCode = AppUtils.getAppVersionCode()
        Timber.d("------> 当前版本号：${versionCode}")
        if (channel == updateInfo.channelFlag && versionCode < updateInfo.clientVersionCode) {
            if (updateInfo.sysClientFiles.isNotEmpty()) {
                view?.showUpdateInfo(updateInfo)
            }
        }
    }
}
package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.barcode.api.BarcodeApi
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.di.qualifier.GetUpgradeUrl
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.update.api.UpdateApi
import com.rouxinpai.arms.update.model.ClientNameEnum
import com.rouxinpai.arms.update.model.ClientTypeEnum
import com.rouxinpai.arms.update.model.UpdateInfo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.RequestBody
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

    @Inject
    @GetUpgradeUrl
    lateinit var updateUrl: String

    private var mLifecycle: Lifecycle? = null

    private val mDisposable = CompositeDisposable()

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
        if (false == view?.isProgressShowing()) {
            view?.showProgress()
            val disposable = retrofit.create<BarcodeApi>()
                .getBarcodeInfo(body = genGetBarcodeInfoBody(barcode))
                .compose(schedulersTransformer())
                .compose(responseTransformer())
                .map { BarcodeInfoVO.convertFromDTO(it) }
                .subscribeWith(object : DefaultObserver<BarcodeInfoVO>(view, false) {

                    override fun onData(t: BarcodeInfoVO) {
                        handleBarcodeInfo(t)
                    }
                })
            addDisposable(disposable)
        } else {
            Timber.d("------> 当前有条码信息正在解析中...")
        }
    }

    override fun getUpdateInfo(
        clientType: ClientTypeEnum,
        clientName: ClientNameEnum,
        channel: String,
    ) {
        val disposable = retrofit.create<UpdateApi>()
            .getUpdateInfo(
                url = updateUrl,
                clientType = clientType.value,
                clientName = clientName.value
            )
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .subscribeWith(object : DefaultObserver<UpdateInfo>(view, false) {

                override fun onData(t: UpdateInfo) {
                    handleUpgradeInfo(channel, t)
                }

                override fun onFail(e: Throwable) {}
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
     * 生成获取条码上下文信息入参
     */
    open fun genGetBarcodeInfoBody(barcode: String): RequestBody {
        val jsonObject = JsonObject().apply {
            addProperty("barCode", barcode)
            add("billTypes", JsonArray().apply {
                add("quantity") // 库存数量
                add("quality") // 质检信息
                add("inboundNo") // 入库单信息
                add("supplierCode") // 供应商信息
            })
        }
        return jsonObject.toRequestBody()
    }

    /**
     * 处理条码上下文数据
     */
    open fun handleBarcodeInfo(barcodeInfo: BarcodeInfoVO) {
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
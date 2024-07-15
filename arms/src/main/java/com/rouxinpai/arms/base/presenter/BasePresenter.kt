package com.rouxinpai.arms.base.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.AppUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.barcode.api.BarcodeApi
import com.rouxinpai.arms.barcode.model.BarcodeInfoVO
import com.rouxinpai.arms.barcode.model.BillType
import com.rouxinpai.arms.barcode.model.BillTypeEnum
import com.rouxinpai.arms.base.view.IView
import com.rouxinpai.arms.dict.api.DictApi
import com.rouxinpai.arms.dict.model.CustomerDictEnum
import com.rouxinpai.arms.dict.model.CustomerDictItemVO
import com.rouxinpai.arms.dict.model.DictEnum
import com.rouxinpai.arms.dict.model.DictItemVO
import com.rouxinpai.arms.dict.model.SysNormalDisableEnum
import com.rouxinpai.arms.dict.util.DictUtil
import com.rouxinpai.arms.extension.eq
import com.rouxinpai.arms.extension.oneOf
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.update.api.UpdateApi
import com.rouxinpai.arms.update.model.ClientNameEnum
import com.rouxinpai.arms.update.model.ClientTypeEnum
import com.rouxinpai.arms.update.model.UpdateInfo
import io.reactivex.rxjava3.core.Observable
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

    override fun listDictItems(showProgress: Boolean, onFail: ((e: Throwable) -> Unit)?) {
        if (showProgress) view?.showProgress()
        // 系统字典数据
        val dictBody = JsonObject().apply {
            addProperty("orderBy", "dictSort")
            addProperty("isAsc", true)
            val queryFields = JsonArray().apply {
                add("status" eq SysNormalDisableEnum.NORMAL.status) // 查询正常状态的数据
                val codes = DictEnum.entries.joinToString(",") { it.code }
                add("dictType" oneOf codes)
            }
            add("queryFields", queryFields)
        }.toRequestBody()
        val dictObserver = retrofit.create<DictApi>()
            .listDictItems(body = dictBody)
            .compose(responseTransformer())
            .map { list -> list.map { dto -> DictItemVO.fromDto(dto) } }
        // 客户自定义字典数据
        val customerDictBody = JsonObject().apply {
            val queryFields = JsonArray().apply {
                add("status" eq SysNormalDisableEnum.NORMAL.status) // 查询正常状态的数据
                val codes = CustomerDictEnum.entries.joinToString(",") { it.code }
                add("code" oneOf codes)
            }
            add("queryFields", queryFields)
        }.toRequestBody()
        val customerDictObserver = retrofit.create<DictApi>()
            .listCustomerDictItems(body = customerDictBody)
            .compose(responseTransformer())
            .map { data -> data.map { CustomerDictItemVO.fromDto(it) } }
        // 合并请求
        val disposable = Observable.zip(
            dictObserver,
            customerDictObserver
        ) { dictList, customerDictList ->
            Pair(dictList, customerDictList)
        }
            .compose(schedulersTransformer())
            .subscribeWith(object :
                DefaultObserver<Pair<List<DictItemVO>, List<CustomerDictItemVO>>>(view) {

                override fun onData(t: Pair<List<DictItemVO>, List<CustomerDictItemVO>>) {
                    super.onData(t)
                    handleDictData(t.first)
                    handleCustomerDictData(t.second)
                    // 关闭进度条
                    if (showProgress) view?.dismissProgress()
                }

                override fun onFail(e: Throwable) {
                    if (onFail != null) {
                        onFail.invoke(e)
                        return
                    }
                    super.onFail(e)
                }
            })
        addDisposable(disposable)
    }

    /**
     * 处理系统字典数据
     */
    open fun handleDictData(items: List<DictItemVO>) {
        // 缓存系统字典数据
        DictUtil.getInstance().putDictData(items)
    }

    /**
     * 处理客户自定义字典数据
     */
    open fun handleCustomerDictData(items: List<CustomerDictItemVO>) {
        // 缓存客户自定义字典数据
        DictUtil.getInstance().putCustomerDictData(items)
    }

    override fun getBarcodeInfo(barcode: String) {
        if (false == view?.isProgressShowing()) {
            view?.showProgress()
            val disposable = retrofit.create<BarcodeApi>()
                .getBarcodeInfo(body = genGetBarcodeInfoBody(barcode))
                .compose(responseTransformer())
                .map { BarcodeInfoVO.convertFromDTO(it) }
                .compose(schedulersTransformer())
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
        onFail: ((e: Throwable) -> Unit)?
    ) {
        val disposable = retrofit.create<UpdateApi>()
            .getUpdateInfo(
                clientType = clientType.value,
                clientName = clientName.value
            )
            .compose(responseTransformer())
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<UpdateInfo>(view, false) {

                override fun onData(t: UpdateInfo) {
                    handleUpgradeInfo(channel, t)
                }

                override fun onFail(e: Throwable) {
                    if (onFail != null) {
                        onFail.invoke(e)
                        return
                    }
                    super.onFail(e)
                }
            })
        addDisposable(disposable)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        mDisposable.clear() // 清除所有订阅
        mLifecycle?.removeObserver(this) // 移除生命周期监听
        mLifecycle = null // 清除生命周期引用
        view = null // 清除 View 引用
        super.onDestroy(owner)
    }

    /**
     * 生成获取条码上下文信息入参
     */
    open fun genGetBarcodeInfoBody(barcode: String): RequestBody {
        val annotation = this::class.java.getAnnotation(BillType::class.java)
        val billTypes = annotation?.billTypeEnums?.asList() ?: BillTypeEnum.entries
        val jsonObject = JsonObject().apply {
            addProperty("barCode", barcode)
            add("billTypes", JsonArray().apply {
                billTypes.forEach { add(it.billTypeCode) }
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
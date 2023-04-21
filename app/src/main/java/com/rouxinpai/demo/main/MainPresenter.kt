package com.rouxinpai.demo.main

import androidx.lifecycle.LifecycleOwner
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.*
import com.rouxinpai.arms.model.*
import com.rouxinpai.demo.http.Api
import okhttp3.RequestBody
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2022/11/17 16:05
 * desc   :
 */
class MainPresenter @Inject constructor() : BasePresenter<MainContract.View>(),
    MainContract.Presenter {

    override fun onCreate(owner: LifecycleOwner) {
        super<BasePresenter>.onCreate(owner)
        getUpdateInfo(clientName = "test", channel = "TestChannel")
    }

    override fun print(text: String) {
        val start = System.currentTimeMillis()
        //
        val service = retrofit.create<Api>()
        // 获取升级信息
        val flowA = service.getUpgradeInfo().compose(responseTransformer())
        // 客户自定义字典数据
        val jsonObjectB = JsonObject().apply {
            val queryFields = JsonArray().apply {
                add("code" eq "material")
            }
            add("queryFields", queryFields)
        }
        val bodyB = jsonObjectB.toRequestBody()
        val flowB = service.listCustomerDictData(bodyB).compose(responseTransformer())
        // 客户自定义字典数据
        val jsonObjectC = JsonObject().apply {
            val queryFields = JsonArray().apply {
                add("code" oneOf "is_advent_date,material_unit,warehouse_type,material_type,material_classification")
            }
            add("queryFields", queryFields)
        }
        val bodyC = jsonObjectC.toRequestBody()
        val flowC = service.listCustomerDictData(bodyC).compose(responseTransformer())
        // 到货单列表
        val flowD = service.listArrivalOrders(getBody()).compose(pagingResponseTransformer())
        //
//        val disposable = Flowable.zip(flowA, flowB, flowC, flowD) { a, b, c, d ->
//            Timber.d("1 ---> 合并数据")
//            Quadruple(a, b, c, d)
//        }
//            .compose(schedulersTransformer())
//            .flatMap {
//                Timber.d("2 ---> 处理数据")
//                Flowable.just(it)
//            }
//            .subscribeWith(object :
//                BaseSubscriber<Quadruple<UpdateInfo, List<CustomerDictDataDTO>, List<CustomerDictDataDTO>, PagingData<List<ArrivalOrderDTO>>>>(view) {
//
//                override fun onData(t: Quadruple<UpdateInfo, List<CustomerDictDataDTO>, List<CustomerDictDataDTO>, PagingData<List<ArrivalOrderDTO>>>) {
//                    val first = t.first
//                    Timber.d("3 ------> 获取升级信息：$first")
//                    val second = t.second
//                    Timber.d("4 ------> 客户自定义字典数据：$second")
//                    val third = t.third
//                    Timber.d("5 ------> 客户自定义字典数据：$third")
//                    val fourth = t.fourth
//                    Timber.d("6 ------> 到货单列表：$fourth")
//                }
//
//                override fun onEmpty() {
//                    super.onEmpty()
//                    Timber.d("7 ------> 某接口请求成功但返回数据为空")
//                }
//
//                override fun onComplete() {
//                    super.onComplete()
//                    Timber.d("8 ------> 所有接口请求完成")
//                }
//
//                override fun onError(e: Throwable) {
//                    super.onError(e)
//                    Timber.d("9 ------> 请求失败：$e")
//                }
//            })
//        addDisposable(disposable)

//        val disposable = service.getUpgradeInfo()
//            .compose(schedulersTransformer())
//            .compose(responseTransformer())
//            .flatMap {
//                Timber.d("1 ---> 处理数据")
//                service.listCustomerDictData(bodyC).compose(responseTransformer())
//            }
//            .subscribeWith(object : DefaultObserver<List<CustomerDictDataDTO>>(view) {
//
//                override fun onData(t: List<CustomerDictDataDTO>) {
//                    super.onData(t)
//                }
//
//                override fun onEmpty() {
//                    super.onEmpty()
//                }
//            })
//        addDisposable(disposable)

//        val disposable = flowC
//            .compose(schedulersTransformer())
//            .map { data -> data.map { CustomerDictItemVO.convertFromDto(it) } }
//            .flatMap {
//                val start1 = System.currentTimeMillis()
//                dictBox.removeAll()
//                dictBox.put(it)
//                Timber.d("1 ---> 处理数据耗时：${System.currentTimeMillis() - start1}ms")
//                Flowable.just(it)
//            }
//            .subscribeWith(object : BaseSubscriber<List<CustomerDictItemVO>>(view) {
//                override fun onData(t: List<CustomerDictItemVO>) {
//
//                    val start2 = System.currentTimeMillis()
//                    val data = dictBox.all
//                    // 打印data
//                    Timber.d("2 ---> 查询数据：$data")
//                    Timber.d("3 ---> 查询数据耗时：${System.currentTimeMillis() - start2}ms")
//                }
//
//                override fun onEmpty() {
//                    super.onEmpty()
//
//                }
//
//                override fun onFail(e: Throwable) {
//                    super.onFail(e)
//
//                }
//            })
//        addDisposable(disposable)
    }
}

private fun getBody(): RequestBody {
    val jsonObject = JsonObject().apply {
        addProperty("orderBy", "createTime")
        addProperty("isAsc", false)
        addProperty("pageSize", 10.toString())
        addProperty("pageNum", 1.toString())
        val queryFields = JsonArray().apply {
            add("type" eq "0") // 0-原材料入库  1-生产入库 2-生产退料入库
        }
        add("queryFields", queryFields)
    }
    return jsonObject.toRequestBody()
}

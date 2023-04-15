package com.rouxinpai.demo.main

import androidx.lifecycle.LifecycleOwner
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.*
import com.rouxinpai.arms.model.*
import com.rouxinpai.arms.model.bean.PagingData
import com.rouxinpai.arms.model.bean.Quadruple
import com.rouxinpai.demo.http.Api
import com.rouxinpai.demo.model.ArrivalOrderDTO
import com.rouxinpai.demo.model.CustomerDictDataDTO
import io.reactivex.rxjava3.core.Flowable
import okhttp3.RequestBody
import retrofit2.create
import timber.log.Timber
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
        // 今日生产数量
        val flowA = service.getCaptcha().compose(responseTransformer())
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
                add("code" eq "material_unit")
            }
            add("queryFields", queryFields)
        }
        val bodyC = jsonObjectC.toRequestBody()
        val flowC = service.listCustomerDictData(bodyC).compose(responseTransformer())
        // 生产任务单列表
        // val flowD = service.listProductions().compose(responseTransformer())
        val flowD = service.listArrivalOrders(getBody()).compose(pagingResponseTransformer())
        //
        val disposable = Flowable.zip(flowA, flowB, flowC, flowD) { a, b, c, d ->
            Timber.d("1 ---> 合并数据")
            Quadruple(a, b, c, d)
        }
            .compose(schedulersTransformer())
            .flatMap {
                Timber.d("2 ---> 处理数据")
                Flowable.just(it)
            }
            .subscribeWith(object :
                DefaultSubscriber<Quadruple<JsonObject, List<CustomerDictDataDTO>, List<CustomerDictDataDTO>, PagingData<List<ArrivalOrderDTO>>>>(
                    view
                ) {

                override fun onNext(t: Quadruple<JsonObject, List<CustomerDictDataDTO>, List<CustomerDictDataDTO>, PagingData<List<ArrivalOrderDTO>>>) {
                    Timber.d("2 ---> 请求成功：${t.first}")
                    Timber.d("2 ---> 请求成功：${t.second}")
                    Timber.d("2 ---> 请求成功：${t.third}")
                    Timber.d("2 ---> 请求成功：${t.fourth}")
                    Timber.d("2 ---> 请求成功：${System.currentTimeMillis() - start}")
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    Timber.d("请求失败：${e.message}")
                }

                override fun onComplete() {
                    super.onComplete()
                    Timber.d("请求完成")
                }
            })
        addDisposable(disposable)
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
}
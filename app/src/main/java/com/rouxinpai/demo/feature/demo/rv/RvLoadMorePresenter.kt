package com.rouxinpai.demo.feature.demo.rv

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.isAsc
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.bean.PagingData
import com.rouxinpai.arms.model.pagingResponseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.demo.model.entity.demo.rv.MaterialDTO
import com.rouxinpai.demo.model.remote.Api
import okhttp3.RequestBody
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/6 10:00
 * desc   :
 */
class RvLoadMorePresenter @Inject constructor() : BasePresenter<RvLoadMoreContract.View>(), RvLoadMoreContract.Presenter {

    companion object {
        // 每页数据长度
        private const val PAGE_SIZE = 20
    }

    // 页码
    private var mPageNum: Int = 1

    override fun listMaterials() {
        view?.showLoadingPage()
        // view?.resetLoadMoreState()
        mPageNum = 1 // 重置页码
        val body = genBody() // 拼装请求参数
        val disposable = retrofit.create<Api>()
            .listMaterials(body = body)
            .compose(schedulersTransformer())
            .compose(pagingResponseTransformer())
            .subscribeWith(object : DefaultObserver<PagingData<List<MaterialDTO>>>(view) {

                override fun onData(t: PagingData<List<MaterialDTO>>) {
                    super.onData(t)
                    val list = t.list
                    if (list.isEmpty()) {
                        view?.showEmptyPage()
                    } else {
                        view?.showMaterialList(list)
                        view?.showSuccessPage()
                        val noMore = PAGE_SIZE >= t.total
                        if (noMore) {
                            view?.loadMoreEnd()
                        } else {
                            view?.loadMoreComplete()
                        }
                    }
                }
            })
        addDisposable(disposable)
    }

    override fun listMoreMaterials() {
        mPageNum++ // 页码+1
        val body = genBody() // 拼装请求参数
        val disposable = retrofit.create<Api>()
            .listMaterials(body = body)
            .compose(schedulersTransformer())
            .compose(pagingResponseTransformer())
            .subscribeWith(object : DefaultObserver<PagingData<List<MaterialDTO>>>(view, false) {

                override fun onData(t: PagingData<List<MaterialDTO>>) {
                    super.onData(t)
                    val list = t.list
                    view?.showMoreMaterialList(list)
                    val noMore = PAGE_SIZE * (mPageNum - 1) + list.size >= t.total
                    if (noMore) {
                        view?.loadMoreEnd()
                    } else {
                        view?.loadMoreComplete()
                    }
                }

                override fun onFail(e: Throwable) {
                    super.onFail(e)
                    mPageNum--
                    view?.loadMoreFail(e)
                }
            })
        addDisposable(disposable)
    }

    // 拼装请求体
    private fun genBody(): RequestBody {
        return JsonObject().apply {
            // 分页
            addProperty("pageNum", mPageNum)
            addProperty("pageSize", PAGE_SIZE)
            // 排序
            val orderBys = JsonArray().apply {
                add("createTime" isAsc false)
            }
            add("orderBys", orderBys)
        }.toRequestBody()
    }
}
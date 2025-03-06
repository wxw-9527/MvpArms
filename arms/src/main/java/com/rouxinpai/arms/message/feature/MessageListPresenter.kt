package com.rouxinpai.arms.message.feature

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.eq
import com.rouxinpai.arms.extension.isAsc
import com.rouxinpai.arms.extension.like
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.message.api.MessageApi
import com.rouxinpai.arms.message.model.MessageDTO
import com.rouxinpai.arms.message.model.MessageVO
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.bean.PagingData
import com.rouxinpai.arms.model.pagingResponseTransformer
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import okhttp3.RequestBody
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 17:00
 * desc   :
 */
class MessageListPresenter @Inject constructor() : BasePresenter<MessageListContract.View>(),
    MessageListContract.Presenter {

    companion object {
        // 每页数据长度
        private const val PAGE_SIZE = 20
    }

    // 页码
    private var mPageNum: Int = 1

    override fun listMessages(title: String?, status: Int?) {
        view?.showLoadingPage()
        mPageNum = 1 // 重置页码
        val body = genBody(title, status) // 拼装请求参数
        val disposable = retrofit.create<MessageApi>()
            .listMessages(body = body)
            .compose(pagingResponseTransformer())
            .map(::convert)
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<PagingData<List<MessageVO>>>(view) {

                override fun onNext(t: PagingData<List<MessageVO>>) {
                    val list = t.list
                    if (list.isEmpty()) {
                        view?.showEmptyPage()
                    } else {
                        view?.showMessageList(list)
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

    override fun listMoreMessages(title: String?, status: Int?) {
        mPageNum++ // 页码+1
        val body = genBody(title, status) // 拼装请求参数
        val disposable = retrofit.create<MessageApi>()
            .listMessages(body = body)
            .compose(pagingResponseTransformer())
            .map(::convert)
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<PagingData<List<MessageVO>>>(view, false) {

                override fun onNext(t: PagingData<List<MessageVO>>) {
                    val list = t.list
                    view?.showMoreMessageList(list)
                    val noMore = PAGE_SIZE * (mPageNum - 1) + list.size >= t.total
                    if (noMore) {
                        view?.loadMoreEnd()
                    } else {
                        view?.loadMoreComplete()
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mPageNum--
                    view?.loadMoreFail(e)
                }
            })
        addDisposable(disposable)
    }

    // 生成请求体
    private fun genBody(title: String?, status: Int?): RequestBody {
        return JsonObject().apply {
            // 分页
            addProperty("pageNum", mPageNum)
            addProperty("pageSize", PAGE_SIZE)
            // 排序
            val orderBys = JsonArray().apply {
                add("createTime" isAsc false) // 创建时间
            }
            add("orderBys", orderBys)
            // 筛选
            val queryFields = JsonArray().apply {
                if (!title.isNullOrBlank()) {
                    add("msgTitle" like title)
                }
                if (status != null) {
                    add("msgStatus" eq status.toString())
                }
            }
            add("queryFields", queryFields)
        }.toRequestBody()
    }

    // 转换数据
    private fun convert(data: PagingData<List<MessageDTO>>): PagingData<List<MessageVO>> {
        val total = data.total
        val list = data.list.map { MessageVO.fromDto(it) }
        return PagingData(total, list)
    }

    override fun getMessage(position: Int, id: String) {
        val body = JsonObject().apply {
            // 筛选
            val queryFields = JsonArray().apply {
                // 查询指定单据
                add("id" eq id)
            }
            add("queryFields", queryFields)
        }.toRequestBody()
        val disposable = retrofit.create<MessageApi>()
            .listMessages(body = body)
            .compose(responseTransformer())
            .map { list -> MessageVO.fromDto(list.first()) }
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<MessageVO>(view, false) {

                override fun onNext(t: MessageVO) {
                    view?.refreshItemData(position, t)
                }
            })
        addDisposable(disposable)
    }

    override fun updateStatus(position: Int, id: String, status: Int) {
        view?.showProgress()
        val body = JsonObject().apply {
            // 需要更新状态的消息id
            val ids = JsonArray().apply { add(id) }
            add("ids", ids)
            // 状态
            addProperty("status", status)
        }.toRequestBody()
        val disposable = retrofit.create<MessageApi>()
            .updateStatus(body = body)
            .compose(responseTransformer())
            .compose(schedulersTransformer())
            .subscribeWith(object : DefaultObserver<String>(view, false) {

                override fun onNext(t: String) {
                    // 关闭进度条
                    view?.dismissProgress()
                    // 更新消息状态
                    getMessage(position, id)
                }
            })
        addDisposable(disposable)
    }
}
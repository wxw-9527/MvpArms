package com.rouxinpai.arms.print

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rouxinpai.arms.barcode.api.BarcodeApi
import com.rouxinpai.arms.barcode.model.BarTypeEnum
import com.rouxinpai.arms.barcode.model.BillTypeEnum
import com.rouxinpai.arms.base.presenter.BasePresenter
import com.rouxinpai.arms.extension.toRequestBody
import com.rouxinpai.arms.model.DefaultObserver
import com.rouxinpai.arms.model.responseTransformer
import com.rouxinpai.arms.model.schedulersTransformer
import com.rouxinpai.arms.print.model.BarcodeVO
import com.rouxinpai.arms.print.model.MaterialVO
import retrofit2.create
import javax.inject.Inject

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/4/1 10:25
 * desc   :
 */
class SelectBarcodePresenter @Inject constructor() : BasePresenter<SelectBarcodeContract.View>(),
    SelectBarcodeContract.Presenter {

    override fun listBarcodeInfos(barcodeList: List<String>) {
        view?.showLoadingPage()
        val body = JsonObject().apply {
            val barCodes = JsonArray().apply { barcodeList.forEach { barcode -> add(barcode) } }
            add("barCodes", barCodes)
            add("billTypes", JsonArray().apply {
                add(BillTypeEnum.SUPPLIER_CODE.billTypeCode)
            })
        }.toRequestBody()
        val disposable = retrofit.create<BarcodeApi>()
            .listBarcodeInfos(body = body)
            .compose(schedulersTransformer())
            .compose(responseTransformer())
            .map { dtoList ->
                val materialVoList = arrayListOf<MaterialVO>()
                val materialMap = mutableMapOf<Pair<String?, String?>, MaterialVO>()
                val materialBarTypeSet = BarTypeEnum.materialEnumList.map { it.barType }.toSet() // 提前转换并使用Set提高查找效率
                dtoList
                    .filter { dto -> dto.barType in materialBarTypeSet } // 过滤物料条码
                    .forEach { dto ->
                        val key = Pair(dto.materialInfo.materialCode, dto.supplierVO?.supplierCode)
                        val barcodeVo = BarcodeVO(dto.barCode)
                        val materialVo = materialMap.getOrPut(key) {
                            // 如果Map中不存在，则创建新的MaterialVO并加入到Map和List中
                            MaterialVO.fromDto(dto).also { materialVoList.add(it) }
                        }
                        materialVo.barcodeList.add(barcodeVo)
                    }
                materialVoList.sortedBy { it.code } // 重新排序
            }
            .subscribeWith(object : DefaultObserver<List<MaterialVO>>(view) {

                override fun onData(t: List<MaterialVO>) {
                    super.onData(t)
                    if (t.isEmpty()) {
                        view?.showEmptyPage()
                        return
                    }
                    view?.showMaterialList(t)
                    view?.showSuccessPage()
                }
            })
        addDisposable(disposable)
    }
}
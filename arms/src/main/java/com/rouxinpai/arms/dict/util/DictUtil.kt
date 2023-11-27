package com.rouxinpai.arms.dict.util

import android.content.Context
import com.rouxinpai.arms.dict.model.CustomerDictEnum
import com.rouxinpai.arms.dict.model.CustomerDictItemVO
import com.rouxinpai.arms.dict.model.CustomerDictItemVO_
import com.rouxinpai.arms.dict.model.DictEnum
import com.rouxinpai.arms.dict.model.DictItemVO
import com.rouxinpai.arms.dict.model.DictItemVO_
import com.rouxinpai.arms.dict.model.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.Admin
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.equal
import timber.log.Timber

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/24 16:56
 * desc   :
 */
class DictUtil {

    companion object {

        @Volatile
        private var instance: DictUtil? = null

        /**
         * 当前类单例对象
         */
        fun getInstance(): DictUtil =
            instance ?: synchronized(this) {
                instance ?: DictUtil().also { instance = it }
            }
    }

    /* *************************************** 初始化相关 *************************************** */

    /**
     * ObjectBox数据库
     */
    lateinit var store: BoxStore
        private set

    /**
     * 初始化
     */
    fun init(context: Context, isDebug: Boolean) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
        if (isDebug) {
            val started = Admin(store).start(context)
            Timber.d("------> ObjectBoxAdmin Started: $started")
        }
    }

    /* *************************************** 系统字典 *************************************** */

    // 系统字典
    private val mDictBox by lazy(LazyThreadSafetyMode.NONE) { store.boxFor<DictItemVO>() }

    /**
     * 缓存系统字典数据
     */
    fun putDictData(list: List<DictItemVO>) {
        mDictBox.removeAll()
        mDictBox.put(list)
    }

    /**
     * 异常类型字典
     */
    val abnormalTypeList: List<DictItemVO> by lazy(LazyThreadSafetyMode.NONE) {
        mDictBox
            .query(DictItemVO_.dictType equal DictEnum.ABNORMAL_TYPE.code)
            .build()
            .find()
    }

    /**
     * 转换异常类型
     */
    fun convertAbnormalType(abnormalType: String?): DictItemVO? {
        if (abnormalType == null) return null
        return abnormalTypeList.find { abnormalType == it.dictValue }
    }

    /**
     * 质检任务状态字典
     */
    val qualityTaskStatusList: List<DictItemVO> by lazy(LazyThreadSafetyMode.NONE) {
        mDictBox
            .query(DictItemVO_.dictType equal DictEnum.QUALITY_TASK_STATUS.code)
            .build()
            .find()
    }

    /**
     * 转换质检任务状态
     */
    fun convertQualityTaskStatus(status: String?): DictItemVO? {
        if (status == null) return null
        return qualityTaskStatusList.find { status == it.dictValue }
    }

    /* *************************************** 客户自定义字典 *************************************** */

    // 客户自定义数据字典
    private val mCustomerDictBox by lazy(LazyThreadSafetyMode.NONE) { store.boxFor<CustomerDictItemVO>() }

    /**
     * 缓存客户自定义数据字典
     */
    fun putCustomerDictData(list: List<CustomerDictItemVO>) {
        mCustomerDictBox.removeAll()
        mCustomerDictBox.put(list)
    }

    /**
     * 物料单位字典
     */
    val materialUnitList by lazy(LazyThreadSafetyMode.NONE) {
        mCustomerDictBox
            .query(CustomerDictItemVO_.code equal CustomerDictEnum.MATERIAL_UNIT.code)
            .build()
            .find()
    }

    /**
     * 转换物料单位
     */
    fun convertMaterialUnit(unit: String?): CustomerDictItemVO? {
        return materialUnitList.find { it.key == unit }
    }

    /**
     * 物料颜色字典
     */
    val materialColorList by lazy(LazyThreadSafetyMode.NONE) {
        mCustomerDictBox
            .query(CustomerDictItemVO_.code equal CustomerDictEnum.MATERIAL_COLOR.code)
            .build()
            .find()
    }

    /**
     * 转换物料颜色
     */
    fun convertMaterialColor(color: String?): CustomerDictItemVO? {
        return materialColorList.find { it.key == color }
    }

    /**
     * 判定方法字典
     */
    val judgeMethodList by lazy(LazyThreadSafetyMode.NONE) {
        mCustomerDictBox
            .query(CustomerDictItemVO_.code equal CustomerDictEnum.JUDGE_METHOD.code)
            .build()
            .find()
    }

    /**
     * 转换判定方法
     */
    fun convertJudgeMethod(method: String?): CustomerDictItemVO? {
        return judgeMethodList.find { it.key == method }
    }

    /**
     * 转换判定方法
     */
    fun convertJudgeMethod(array: Array<String>): List<CustomerDictItemVO> {
        return array.mapNotNull { convertJudgeMethod(it) }
    }
}
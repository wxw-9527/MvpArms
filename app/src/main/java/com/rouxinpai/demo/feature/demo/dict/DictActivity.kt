package com.rouxinpai.demo.feature.demo.dict

import android.os.Bundle
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.dict.util.DictUtil
import com.rouxinpai.demo.databinding.DictActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/11/24 17:30
 * desc   :
 */
@AndroidEntryPoint
class DictActivity : BaseMvpActivity<DictActivityBinding, DictContract.View, DictPresenter>(),
    DictContract.View {

    override fun showDictData() {
        binding.tvDict.text = buildString {
            // 申报异常类型
            appendLine("申报异常类型")
            appendLine()
            DictUtil.getInstance().abnormalTypeList.forEach {
                appendLine(it.dictLabel)
            }
            appendLine()
            appendLine()
            // 质检任务状态
            appendLine("质检任务状态")
            appendLine()
            DictUtil.getInstance().qualityTaskStatusList.forEach {
                appendLine(it.dictLabel)
            }
            appendLine()
            appendLine()
            // 赋码规则字典
            appendLine("赋码规则字典")
            appendLine()
            DictUtil.getInstance().codingRuleList.forEach {
                appendLine(it.dictLabel)
            }
            appendLine()
            appendLine()
            appendLine()
            appendLine()
            // 物料单位字典
            appendLine("物料单位字典")
            appendLine()
            DictUtil.getInstance().materialUnitList.forEach {
                appendLine(it.value)
            }
            appendLine()
            appendLine()
            // 物料颜色字典
            appendLine("物料颜色字典")
            appendLine()
            DictUtil.getInstance().materialColorList.forEach {
                appendLine(it.value)
            }
            appendLine()
            appendLine()
            // 判定方法字典
            appendLine("判定方法字典")
            appendLine()
            DictUtil.getInstance().judgeMethodList.forEach {
                appendLine(it.value)
            }
            appendLine()
            appendLine()
        }
    }
}
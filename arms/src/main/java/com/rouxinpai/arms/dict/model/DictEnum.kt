package com.rouxinpai.arms.dict.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/9/26 11:19
 * desc   :
 */
enum class DictEnum(val code: String) {

    /**
     * 申报异常类型
     */
    ABNORMAL_TYPE("abnormal_type"), // 生产看板

    /**
     * 质检任务状态
     */
    QUALITY_TASK_STATUS("quality_task_status"), // 质检

    /**
     * 赋码规则
     */
    CODING_RULE("coding_rule"), // 仓储
}
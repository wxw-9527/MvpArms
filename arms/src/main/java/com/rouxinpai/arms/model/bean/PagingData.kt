package com.rouxinpai.arms.model.bean

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/4/14 11:46
 * desc   :
 */
data class PagingData<T : Collection<*>>(val total: Int, val list: T)
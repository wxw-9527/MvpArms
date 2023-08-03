package com.rouxinpai.demo.feature.demo.dropdown

import com.rouxinpai.arms.view.DropdownButton

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/3 16:28
 * desc   :
 */
data class DropdownEntity(val code: String, val name: String) : DropdownButton.IDropdownEntity {
    override fun getShowText(): String {
        return name
    }
}
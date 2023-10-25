package com.rouxinpai.arms.print.factory

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/10/24 11:10
 * desc   :
 */
interface OnConnectListener {
    fun onConnectStart()
    fun onConnectSuccessful()
    fun onConnectClosed()
    fun onConnectFail()

}
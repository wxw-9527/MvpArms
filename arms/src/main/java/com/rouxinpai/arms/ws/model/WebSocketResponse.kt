package com.rouxinpai.arms.ws.model

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/29 23:02
 * desc   :
 */
data class WebSocketResponse<T>(val modelName: String, val functionName: String, val params: T?) {

    companion object {

        // 模块名
        private const val MODEL_NAME = "cloud-wms-service"

        // 多人拣货操作初始消息
        const val MULTI_PICKER = "/outboundOrderDetailRecordDraft/getMsg"

        /**
         * 多人拣货操作初始消息
         */
        fun multiplayerPickingInitialMessage(outboundOrderId: String, materialCode: String): WebSocketResponse<Map<String, String>> {
            return WebSocketResponse(
                modelName = MODEL_NAME,
                functionName = MULTI_PICKER,
                params = mapOf("outboundOrderId" to outboundOrderId, "materialCode" to materialCode)
            )
        }
    }
}
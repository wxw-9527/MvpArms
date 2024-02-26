package com.rouxinpai.arms.message.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.databinding.MessageDetailActivityBinding
import com.rouxinpai.arms.message.model.MessageVO
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/22 15:16
 * desc   :
 */
@AndroidEntryPoint
class MessageDetailActivity :
    BaseMvpActivity<MessageDetailActivityBinding, MessageDetailContract.View, MessageDetailPresenter>(),
    MessageDetailContract.View {

    companion object {

        // 参数传递标志
        private const val ARG_MESSAGE_ID = "arg_message_id" // 消息id
        private const val ARG_MESSAGE_TITLE = "arg_message_title" // 消息标题

        /**
         * 启动[MessageDetailActivity]页
         */
        @JvmStatic
        fun start(
            context: Context,
            messageId: String,
            messageTitle: String,
            launcher: ActivityResultLauncher<Intent>,
        ) {
            val starter = Intent(context, MessageDetailActivity::class.java).apply {
                val bundle = bundleOf(
                    ARG_MESSAGE_ID to messageId,
                    ARG_MESSAGE_TITLE to messageTitle
                )
                putExtras(bundle)
            }
            launcher.launch(starter)
        }
    }

    // 消息id
    private lateinit var mMessageId: String

    // 消息标题
    private lateinit var mMessageTitle: String

    override fun onParseData(bundle: Bundle) {
        super.onParseData(bundle)
        mMessageId = bundle.getString(ARG_MESSAGE_ID).orEmpty()
        mMessageTitle = bundle.getString(ARG_MESSAGE_TITLE).orEmpty()
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 渲染界面标题
        title = mMessageTitle
        // 绑定监听事件
        onBackPressedDispatcher.addCallback(this, mOnBackPressedCallback)
        // 获取消息详情
        presenter.getMessage(mMessageId)
    }

    override fun showMessage(messageVO: MessageVO) {
        binding.tvMessage.text = messageVO.msgContent
        binding.tvCreateTime.text = messageVO.createTime
        binding.tvCreateBy.text = messageVO.createBy
    }

    // 返回事件拦截
    private val mOnBackPressedCallback = object : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }
    }
}
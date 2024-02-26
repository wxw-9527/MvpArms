package com.rouxinpai.arms.message.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.view.isInvisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.kongzue.dialogx.dialogs.MessageDialog
import com.rouxinpai.arms.R
import com.rouxinpai.arms.base.activity.BaseMvpActivity
import com.rouxinpai.arms.base.adapter.BaseVbAdapter
import com.rouxinpai.arms.databinding.MessageListActivityBinding
import com.rouxinpai.arms.databinding.MessageListRecycleItemBinding
import com.rouxinpai.arms.message.model.MessageStatusEnum
import com.rouxinpai.arms.message.model.MessageStatusEnum.*
import com.rouxinpai.arms.message.model.MessageVO
import dagger.hilt.android.AndroidEntryPoint

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 16:43
 * desc   :
 */
@AndroidEntryPoint
class MessageListActivity :
    BaseMvpActivity<MessageListActivityBinding, MessageListContract.View, MessageListPresenter>(),
    MessageListContract.View,
    OnRefreshListener, TrailingLoadStateAdapter.OnTrailingListener,
    BaseQuickAdapter.OnItemClickListener<MessageVO>,
    BaseQuickAdapter.OnItemLongClickListener<MessageVO> {

    companion object {

        /**
         * 启动[MessageListActivity]页
         */
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MessageListActivity::class.java)
            context.startActivity(starter)
        }
    }

    // 消息列表适配器
    private val mMessageAdapter = MessageAdapter()

    // 待刷新的列表项的位置
    private var mPendingRefreshItemPosition: Int? = null

    // 待刷新的列表项的消息ID
    private var mPendingRefreshId: String? = null

    override val stateLayout: View
        get() = binding.refreshLayout

    override fun onCreateAdapterHelper(): QuickAdapterHelper {
        return QuickAdapterHelper
            .Builder(mMessageAdapter)
            .setTrailingLoadStateAdapter(this)
            .build()
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        // 绑定适配器
        binding.rvMessage.adapter = adapterHelper?.adapter
        // 添加分割线
        dividerBuilder()
            .colorRes(R.color.message_list__divider)
            .size(1, TypedValue.COMPLEX_UNIT_DIP)
            .insetStart(SizeUtils.dp2px(8f))
            .insetEnd(SizeUtils.dp2px(8f))
            .build()
            .addTo(binding.rvMessage)
        // 绑定监听事件
        binding.refreshLayout.setOnRefreshListener(this)
        mMessageAdapter.setOnItemClickListener(this)
        mMessageAdapter.setOnItemLongClickListener(this)
        // 获取数据
        onRefresh()
    }

    override fun showMessageList(list: List<MessageVO>) {
        // 渲染列表数据
        mMessageAdapter.submitList(list)
        // 停止下拉刷新动画
        binding.refreshLayout.isRefreshing = false
    }

    override fun showMoreMessageList(list: List<MessageVO>) {
        // 渲染列表数据
        mMessageAdapter.addAll(list)
    }

    override fun refreshItemData(position: Int, messageVO: MessageVO) {
        mMessageAdapter[position] = messageVO
    }

    override fun onRefresh() {
        presenter.listMessages(null, null)
    }

    override fun onLoad() {
        presenter.listMoreMessages(null, null)
    }

    override fun onFailRetry() {
        presenter.listMoreMessages(null, null)
    }

    override fun onClick(adapter: BaseQuickAdapter<MessageVO, *>, view: View, position: Int) {
        val item = adapter.getItem(position) ?: return
        // 设置待刷新的列表项的位置和订单ID
        mPendingRefreshItemPosition = position
        mPendingRefreshId = item.id
        // 跳转消息详情页
        MessageDetailActivity.start(this, item.id, item.msgTitle, mLauncher)
    }

    private val mLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (RESULT_OK == it.resultCode) {
            val position = mPendingRefreshItemPosition
            val messageId = mPendingRefreshId
            if (position != null && messageId != null) {
                presenter.getMessage(position, messageId)
            }
        }
    }

    override fun onLongClick(
        adapter: BaseQuickAdapter<MessageVO, *>,
        view: View,
        position: Int,
    ): Boolean {
        val item = adapter.getItem(position) ?: return false
        showConfirmationDialog(position, item.id, item.messageStatusEnum)
        return true
    }

    /**
     * 展示确认弹窗
     * @param position 消息在列表中的位置
     * @param id 消息的ID
     * @param status 消息当前的状态
     */
    private fun showConfirmationDialog(position: Int, id: String, status: MessageStatusEnum) {
        val (titleResId, messageResId, newStatus) = when (status) {
            UNREAD -> Triple(
                R.string.message_list__mark_read,
                R.string.message_list__mark_read_message,
                READ.status
            )
            READ -> Triple(
                R.string.message_list__mark_unread,
                R.string.message_list__mark_unread_message,
                UNREAD.status
            )
        }
        MessageDialog.build()
            .setTitle(titleResId)
            .setMessage(messageResId)
            .setCancelButton(R.string.message_list__cancel)
            .setOkButton(R.string.message_list__ok) { _, _ ->
                presenter.updateStatus(position, id, newStatus)
                false
            }
            .show()
    }

    /**
     * 消息列表适配器
     */
    private class MessageAdapter : BaseVbAdapter<MessageListRecycleItemBinding, MessageVO>() {

        override fun onBindView(
            binding: MessageListRecycleItemBinding,
            position: Int,
            item: MessageVO,
        ) {
            // 消息标题
            binding.tvTitle.text = item.msgTitle
            // 状态
            binding.ivUnread.isInvisible = (READ == item.messageStatusEnum) // 已读时隐藏
            // 时间
            binding.tvTime.text = item.friendlyTimeSpanByNow
            // 消息内容
            binding.tvMessage.text = item.msgContent
        }
    }
}
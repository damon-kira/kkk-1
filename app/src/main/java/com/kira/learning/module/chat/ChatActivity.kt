package com.kira.learning.module.chat

import android.os.Bundle
import android.widget.Toast
import com.kira.learning.databinding.ActivityChatBinding
import com.kira.learning.module.chat.ui.ChatViewModel
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

// app/src/main/java/com/example/ai/chatdemo/ui/activity/ChatActivity.kt
@AndroidEntryPoint
class ChatActivity : BaseActivity() {

    private val mBinding by binding<ActivityChatBinding>()
    private val viewModel by lazyViewModel<ChatViewModel>()
    private val adapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mBinding = ActivityMainmBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // 初始化ViewModel
//        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // 设置RecyclerView
        mBinding.chatRecyclerView.adapter = adapter
        setupObservers()

        // 发送按钮点击事件
        mBinding.sendButton.setOnClickListener {
            val message = mBinding.messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message, isUser = true)
            }
        }

        // EditText回车发送
        mBinding.messageEditText.setOnEditorActionListener { _, _, _ ->
            val message = mBinding.messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message, isUser = true)
                true
            } else {
                false
            }
        }
    }

    private fun setupObservers() {
        // 观察聊天记录变化
        viewModel.messages.observe(this) { messages ->
            adapter.submitList(messages)
            scrollToBottom()
        }

        // 观察消息发送结果
        viewModel.sendMessageResult.observe(this) { success ->
            if (!success) {
                Toast.makeText(this, "发送失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessage(message: String, isUser: Boolean) {
        viewModel.sendMessage(message, isUser)
        mBinding.messageEditText.text.clear()
    }

    private fun scrollToBottom() {
        if(adapter.itemCount - 1 >= 0) {
            mBinding.chatRecyclerView.post {
                mBinding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}
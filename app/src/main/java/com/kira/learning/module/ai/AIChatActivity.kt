package com.kira.learning.module.ai

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kira.learning.R
import com.kira.learning.databinding.ActivityAiimBinding
import com.kira.learning.manager.Launch
import com.kira.learning.bean.ChatMessage
import com.kira.learning.bean.Conversation
import com.common.lib.base.BaseActivity
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.image.glide.GlideImagesPlugin
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AIChatActivity : BaseActivity() {

    private val mBinding by binding<ActivityAiimBinding>()

    private val mViewModel by lazyViewModel<AIChatViewModel>()

    private var chatRecyclerView: RecyclerView? = null
    private var messageEditText: EditText? = null
    private var userSend: Button? = null
    private var chatMessages: ArrayList<ChatMessage> = ArrayList<ChatMessage>()
    private var isFinish = false
    private var countDownTimer: CountDownTimer? = null
    private var apiKey: String? = null
    private var conversationId: Long = 0
    private var conversation: Conversation? = null
    private var messageUser: ChatMessage? = null

    lateinit var mAdapter: AIChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setViewModelLoading(mViewModel)
        initObserver()

        chatRecyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        messageEditText = findViewById<EditText>(R.id.messageEditText)
        userSend = findViewById<Button>(R.id.userSend)
        val otherClear = findViewById<ImageView>(R.id.other_clear)
        val brainClear = findViewById<ImageView>(R.id.brain_clear)
        val textBar = findViewById<TextView>(R.id.TextBar)

        val markwon = Markwon.builder(this)
            .usePlugin(GlideImagesPlugin.create(this))
            .build()
        mAdapter = AIChatAdapter(chatMessages, markwon, "kunkun", mBinding.chatRecyclerView).also {
            it.setHasStableIds(true)
        }

        mBinding.chatRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.chatRecyclerView.adapter = mAdapter

        val sharedPreferences = getSharedPreferences("kimiChat", MODE_PRIVATE)
        apiKey = sharedPreferences.getString(
            "API_KEY",
            "sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU"
        )


//        ChatDatabaseHelper(this).use { chatDatabaseHelper ->
//            // 清空对话
//        }
        // 清空对话
        brainClear.setOnClickListener(View.OnClickListener { v: View? ->
            // 保存之前的聊天记录
//            saveConversation(chatDatabaseHelper)
            //dialogClear();
            // 开启新对话
            initChat(sharedPreferences, "kunkun")
            //dialogClear();
            val tipStr = "KunKun已重新启动！你可以向我提出任何问题。"
//            val tipInfo = ChatMessage(tipStr, false, conversationId)

//            chatMessages.add(tipInfo)
            mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1)
            chatRecyclerView!!.smoothScrollToPosition(mAdapter.getItemCount() - 1)

            //改变UI
            userSend!!.setBackground(getDrawable(R.drawable.send_button_select))
        })
        // 英语学习模式
//        eng_learn.setOnClickListener(View.OnClickListener { v: View? ->
//            saveConversation(chatDatabaseHelper)
//            //dialogClear();
//            initChat(sharedPreferences, "english")
//            val tipStr = "KunKun已进入英文句子分析模式!\n" +
//                    "直接输入英文句子,我将会分析句子成分。"
//            val tipInfo = ChatMessage(tipStr, false, conversationId)
//
//            chatMessages.add(tipInfo)
//            mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1)
//            chatRecyclerView!!.smoothScrollToPosition(mAdapter.getItemCount() - 1)
//
//            //改变UI
//            userSend!!.setBackground(getDrawable(R.drawable.user_send_english))
//        })


        // 初始化AI
        initChat(sharedPreferences, "kunkun")

        regionChat()

        // 当用户点击发送按钮时，创建消息并更新 UI
        userSend!!.setOnClickListener(object : View.OnClickListener {
            private var messageBot: ChatMessage? = null
            private var kimiResponse: String? = null

            @SuppressLint("SetTextI18n")
            override fun onClick(v: View?) {
                // 获取文本并创建消息

                val messageText = mBinding.messageEditText.text.toString().trim { it <= ' ' }
                if (!messageText.isEmpty()) {
                    messageUser = ChatMessage(
                        content = messageText,
                        isUser = "USER",
                        conversationId = conversationId
                    )
                    chatMessages.add(messageUser!!)

                    messageBot = ChatMessage(
                        content = "KunKun思考中...",
                        isUser = "BOT",
                        conversationId = conversationId
                    )
                    chatMessages.add(messageBot!!)

                    textBar.text = "60 S"
                    // 初始化isSkip为false,代表是否完成倒计时或kimiResponse已经接收到了解析结果
                    isFinish = false
                    // 设置每1秒更新一次textBar的40秒倒计时，当isFinish为true时，停止倒计时
                    startCountdownTimer()

                    // 等待回复途中,禁止发送消息
                    messageEditText!!.setText("")
                    mBinding.userSend.isEnabled = false
//                    logger_e("debug_","已添加 ${mAdapter.getItemCount()}")

                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1)
                    mBinding.chatRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1)

                    mViewModel.sendMessage(messageText)

                    // 保存当前对话到历史记录
                } else {
                    Toast.makeText(this@AIChatActivity, "请输入内容", Toast.LENGTH_SHORT).show()
                }
            }

            fun startCountdownTimer() {
                // 如果上一个计时器在活动，取消计时器

                if (countDownTimer != null) {
                    countDownTimer!!.cancel()
                }
                countDownTimer = object : CountDownTimer(60000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        if (!isFinish) {
                            // Update textBar every second
                            textBar.setText(
                                String.format(
                                    Locale.getDefault(),
                                    "%d S",
                                    millisUntilFinished / 1000
                                )
                            )
                        }
                    }

                    override fun onFinish() {
                        if (!isFinish) {
                            // 未收到响应时处理超时
//                            messageBot = ChatMessage(
//                                "KunKun的CPU被干烧了，请重新提问吧",
//                                false,
//                                conversationId
//                            )
                            Log.e(TAG, "onFinish: 倒计时结束")
                            userSend!!.setEnabled(true)
                            // 更新并保存消息
                            //AddAMsgToSQLite(messageBot, chatDatabaseHelper);
                        }
                    }
                }

                countDownTimer!!.start()
            }
        })
        // 设置页面
//        otherClear.setOnClickListener(v -> {
//            Intent intent = new Intent(AIIMActivity.this, AboutActivity.class);
//            startActivity(intent);
//        });
        //清除页面
        otherClear.setOnLongClickListener(OnLongClickListener { v: View? ->
            dialogClear()
            Toast.makeText(this@AIChatActivity, "对话内容清除成功", Toast.LENGTH_SHORT).show()
            true
        })

        // 历史对话
//        history_chat.setOnClickListener(v->{
//            Intent intent = new Intent(AIIMActivity.this, HistoryActivity.class);
//            startActivity(intent);
//        });
    }

    private fun regionChat() {
        //20250513422088
        mViewModel.setConversation(20250513422088)
        conversationId = 20250513422088
        lifecycleScope.launch {
            mViewModel.messages.collect { messages ->
                chatMessages.clear()
                chatMessages.addAll(messages)
                if (mAdapter.itemCount > 0) {
                    mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
                    mBinding.chatRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
                }
            }
        }
    }

    private fun initObserver() {
        mViewModel.aiimLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.let { data ->
                    // Success
                    val responseMessage = data.choices[0].message.content
                    Log.e(TAG, "initObserver: 请求成功 ${responseMessage}")
                    chatMessages.removeAt(chatMessages.lastIndex)
                    val messageBot = ChatMessage(
                        content = responseMessage,
                        isUser = "BOT",
                        conversationId = conversationId
                    )
                    chatMessages.add(messageBot)

                    //添加数据库
                    mViewModel.insertMessage(messageUser!!)
                    mViewModel.insertMessage(messageBot)
                    //tempMessageList.add(messageBot);
//                            textBar.setText("Kira")
                    userSend?.isEnabled = true

                    // mAdapter.no tifyDataSetChanged();
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1)
                    mBinding.chatRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1)
                }
            }
        }

        // 观察数据变化
        mViewModel.responses.observe(this) { responses ->
            // 更新 UI（例如显示在 RecyclerView）
        }
    }

    open fun checkOrderResult() {
        Launch.skipMainActivity(this)
        finish()
    }


//    private fun setClick() {
//        mBinding.toolbar.setCustomClickListener {
//            showCustomDialog()
//        }
//        mBinding.toolbar.setOnbackListener { finish() }
//        mBinding.tvApply.setBlockingOnClickListener {
//            click_type = CLICK_REPAY
//            checkOrder()
//        }
//
//        mBinding.tvAmount.setBlockingOnClickListener {
//            detailDialog.show()
//        }
//
//        mBinding.tvExtension.setBlockingOnClickListener {
//            click_type = CLICK_DEFER
//            checkOrder()
//        }
//    }

    override fun onDestroy() {
//        LiveDataBus.removeObserve(PayEvent::class.java, mObserver)
        super.onDestroy()
    }

//    private fun saveConversation(chatDatabaseHelper: ChatDatabaseHelper) {
//        conversation!!.chatMessages.isEmpty().let {
//            if (!it) {
//                if (conversation!!.chatMessages.size > 1) {
//                    chatDatabaseHelper.saveConversation(conversation!!)
//                    Toast.makeText(this, "对话已保存", Toast.LENGTH_SHORT).show()
//                }
//                dialogClear()
//            }
//        }
//    }

    /**
     * 初始化
     */
    private fun initChat(sharedPreferences: SharedPreferences, ChatMode: String) {
        apiKey = sharedPreferences.getString(
            "API_KEY",
            "sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU"
        )
//        mAdapter!!.ChatMode = ChatMode
        val tempStartTimeStamp = System.currentTimeMillis()
        val tempConversationId: Long = RandomCID(tempStartTimeStamp)
        conversation = Conversation(tempConversationId, tempStartTimeStamp, chatMessages, ChatMode)
        conversationId = conversation!!.conversationId // 设置新的对话ID
//        kimi = KiraChatService(ChatMode)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun dialogClear() {
        chatMessages.clear() // 清除所有消息
        mAdapter.notifyDataSetChanged()
        chatRecyclerView!!.smoothScrollToPosition(mAdapter.getItemCount())
    }

    companion object {
        private fun RandomCID(tempStartTimeStamp: Long): Long {
            val randomNumber = (Math.random() * 1000000L).toLong()
            val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val date = Date(tempStartTimeStamp) // 将long时间戳转换为Date对象
            val formattedDateString = formatter.format(date) // 将Date对象格式化为字符串
            return formattedDateString.toLong() * 1000000L + randomNumber
        }
    }
}
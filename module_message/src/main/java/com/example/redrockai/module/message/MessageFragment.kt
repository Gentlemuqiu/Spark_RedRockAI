package com.example.redrockai.module.message

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redrockai.module.message.databinding.FragmentMessageBinding
import com.iflytek.sparkchain.core.LLM
import com.iflytek.sparkchain.core.LLMCallbacks
import com.iflytek.sparkchain.core.LLMConfig
import com.iflytek.sparkchain.core.LLMError
import com.iflytek.sparkchain.core.LLMEvent
import com.iflytek.sparkchain.core.LLMResult
import com.iflytek.sparkchain.core.Memory
import com.iflytek.sparkchain.core.SparkChain


class MessageFragment : Fragment() {
    private val TAG = "AEE"
    private var sessionFinished = true
    private lateinit var llm: LLM
    private lateinit var chatAdapter: MessageAdapter
    private val messages = mutableListOf<ChatMessage>()
    private val accumulatedContent = StringBuilder()
    private var temporaryMessageIndex: Int? = null


    private var llmCallbacks: LLMCallbacks = object : LLMCallbacks {
        override fun onLLMResult(llmResult: LLMResult, usrContext: Any?) {
            val content: String = llmResult.content
            val status: Int = llmResult.status
            activity?.runOnUiThread {
                accumulatedContent.append(content)

                if (status == 2) {
                    // 状态为2，拼接完成，添加新消息
                    if (temporaryMessageIndex != null) {
                        // 如果已经有临时消息，更新它
                        messages[temporaryMessageIndex!!] = ChatMessage(accumulatedContent.toString(), false)
                        chatAdapter.notifyItemChanged(temporaryMessageIndex!!)
                        temporaryMessageIndex = null
                    } else {
                        messages.add(ChatMessage(accumulatedContent.toString(), false))
                        chatAdapter.notifyItemInserted(messages.size - 1)
                    }
                    toEnd()
                    accumulatedContent.clear()
                    sessionFinished = true
                } else {
                    // 状态不是2，更新UI显示正在加载的内容
                    if (temporaryMessageIndex == null) {
                        // 如果没有临时消息，添加一条
                        messages.add(ChatMessage(accumulatedContent.toString(), false))
                        temporaryMessageIndex = messages.size - 1
                        chatAdapter.notifyItemInserted(temporaryMessageIndex!!)
                    } else {
                        // 如果已经有临时消息，更新它
                        messages[temporaryMessageIndex!!] = ChatMessage(accumulatedContent.toString(), false)
                        chatAdapter.notifyItemChanged(temporaryMessageIndex!!)
                    }
                    toEnd()
                }
            }
        }

        override fun onLLMEvent(event: LLMEvent, usrContext: Any?) {
            Log.d(TAG, "onLLMEvent\n")
        }

        override fun onLLMError(error: LLMError, usrContext: Any?) {
            Log.d(TAG, "onLLMError\n")
            activity?.runOnUiThread {
                messages.add(ChatMessage("错误: err:${error.getErrCode()} errDesc:${error.getErrMsg()}", false))
                chatAdapter.notifyItemInserted(messages.size - 1)
                toEnd()
            }
            sessionFinished = true
        }
    }

    private var _mBinding: FragmentMessageBinding? = null
    private val mBinding: FragmentMessageBinding
        get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentMessageBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initButtonClickListener()
        setLLMConfig()
    }

    private fun setLLMConfig() {
        val llmConfig: LLMConfig = LLMConfig.builder()
        llmConfig.domain("generalv3.5")
        llmConfig.url("ws(s)://spark-api.xf-yun.com/v3.5/chat")
        llmConfig.maxToken(8192)
        val window_memory: Memory = Memory.windowMemory(5)
        llm = LLM(llmConfig, window_memory)
        llm.registerLLMCallbacks(llmCallbacks)
    }

    private fun startChat() {
        val usrInputText = mBinding.aigcFragmentAigcEt.text.toString()
        if (usrInputText.isNotEmpty()) {
            messages.add(ChatMessage(usrInputText, true))
            chatAdapter.notifyItemInserted(messages.size - 1)
            mBinding.aigcFragmentAigcEt.clearFocus()
            mBinding.aigcFragmentAigcEt.setText("")
        }
        val myContext = "只能回答教育相关的问题"
        val ret: Int = llm.arun(usrInputText, myContext)
        if (ret != 0) {
            Log.e(TAG, "SparkChain failed:\n$ret")
            return
        }
        sessionFinished = false
    }

    private fun initButtonClickListener() {
        mBinding.aigcFragmentAigcEt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (sessionFinished) {
                startChat()
                toEnd()
                return@OnEditorActionListener true
            } else {
                Toast.makeText(this.context, "Busying! Please Wait", Toast.LENGTH_SHORT).show()
            }
            false
        })
        // 隐藏键盘
        view?.let { v ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun initView() {
        chatAdapter = MessageAdapter(messages)
        mBinding.aigcFragmentAigcRvMessage.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    fun toEnd() {
        mBinding.aigcFragmentAigcRvMessage.scrollToPosition(messages.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
        unInitSDK()
    }

    private fun unInitSDK() {
        SparkChain.getInst().unInit()
    }
}
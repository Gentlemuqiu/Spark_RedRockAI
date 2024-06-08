package com.example.redrock.module.video.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.redrock.module.video.R
import com.iflytek.sparkchain.core.LLM
import com.iflytek.sparkchain.core.LLMCallbacks
import com.iflytek.sparkchain.core.LLMConfig
import com.iflytek.sparkchain.core.LLMError
import com.iflytek.sparkchain.core.LLMEvent
import com.iflytek.sparkchain.core.LLMResult
import com.iflytek.sparkchain.core.Memory
import kotlinx.coroutines.launch


class HomeWorkFragment : Fragment() {

    private lateinit var mHomeWork: TextView


    private val TAG = "AEE"
    private var sessionFinished = true
    private lateinit var llm: LLM
    private val accumulatedContent = StringBuilder()
    private var temporaryMessageIndex: Int? = null


    private var llmCallbacks: LLMCallbacks = object : LLMCallbacks {
        override fun onLLMResult(llmResult: LLMResult, usrContext: Any?) {
            val content: String = llmResult.content
            val status: Int = llmResult.status

            activity?.runOnUiThread {
                accumulatedContent.append(content)
                mHomeWork.append(content)
                sessionFinished = true

            }
        }

        override fun onLLMEvent(event: LLMEvent, usrContext: Any?) {
            Log.d(TAG, "onLLMEvent\n")
        }

        override fun onLLMError(error: LLMError, usrContext: Any?) {
            Log.d(TAG, "onLLMError\n")
            sessionFinished = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_work, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHomeWork = view.findViewById(R.id.homework)
        //建议初始化
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
        startChat()
    }

    private fun startChat() {

        val description = requireActivity().intent.getStringExtra("description")
        val title = requireActivity().intent.getStringExtra("title").toString()

        viewLifecycleOwner.lifecycleScope.launch {
            val usrInputText =
                "给我布置一些作业，我的课程标题是${title},课程描述是${description}"
            val myContext = "只能回答教育相关的问题"
            val ret: Int = llm.arun(usrInputText, myContext)
            if (ret != 0) {
                Log.e(TAG, "SparkChain failed:\n$ret")
            }
            sessionFinished = false
        }

    }


}


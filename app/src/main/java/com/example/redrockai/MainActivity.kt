package com.example.redrockai

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.iflytek.sparkchain.core.LLM
import com.iflytek.sparkchain.core.LLMCallbacks
import com.iflytek.sparkchain.core.LLMConfig
import com.iflytek.sparkchain.core.LLMError
import com.iflytek.sparkchain.core.LLMEvent
import com.iflytek.sparkchain.core.LLMResult
import com.iflytek.sparkchain.core.Memory
import com.iflytek.sparkchain.core.SparkChain
import com.iflytek.sparkchain.core.SparkChainConfig


class MainActivity : AppCompatActivity() {
    private val TAG = "AEE"
    private lateinit var startChatBtn: Button
    private lateinit var chatText: TextView
    private lateinit var inputText: EditText

    // 设定flag，在输出未完成时无法进行发送
    private var sessionFinished = true

    private var llm: LLM? = null
    var llmCallbacks: LLMCallbacks = object : LLMCallbacks {
        override fun onLLMResult(llmResult: LLMResult, usrContext: Any?) {
            Log.d(TAG, "onLLMResult\n")
            val content: String = llmResult.getContent()
            Log.e(TAG, "onLLMResult:$content")
            val status: Int = llmResult.getStatus()
            if (content != null) {
                runOnUiThread {
                    chatText!!.append(content)
                    toend()
                }
            }
            if (usrContext != null) {
                val context = usrContext as String
                Log.d(TAG, "context:$context")
            }
            if (status == 2) {
                val completionTokens: Int = llmResult.getCompletionTokens()
                val promptTokens: Int = llmResult.getPromptTokens() //
                val totalTokens: Int = llmResult.getTotalTokens()
                Log.e(
                    TAG,
                    "completionTokens:" + completionTokens + "promptTokens:" + promptTokens + "totalTokens:" + totalTokens
                )
                sessionFinished = true
            }
        }

        override fun onLLMEvent(event: LLMEvent, usrContext: Any?) {
            Log.d(TAG, "onLLMEvent\n")
            Log.w(TAG, "onLLMEvent:" + " " + event.getEventID() + " " + event.getEventMsg())
        }

        override fun onLLMError(error: LLMError, usrContext: Any?) {
            Log.d(TAG, "onLLMError\n")
            Log.e(TAG, "errCode:" + error.getErrCode() + "errDesc:" + error.getErrMsg())
            runOnUiThread { chatText!!.append("错误:" + " err:" + error.getErrCode() + " errDesc:" + error.getErrMsg() + "\n") }
            if (usrContext != null) {
                val context = usrContext as String
                Log.d(TAG, "context:$context")
            }
            sessionFinished = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
                initView()
                initButtonClickListener()
                initSDK ()
    }
    override fun onDestroy() {
        super.onDestroy()
        unInitSDK()
    }

    private fun setLLMConfig() {
        Log.d(TAG, "setLLMConfig")
        val llmConfig: LLMConfig = LLMConfig.builder()
        llmConfig.domain("generalv3.5")
        llmConfig.url("ws(s)://spark-api.xf-yun.com/v3.5/chat") //必填
        //memory有两种，windows_memory和tokens_memory，二选一即可
        val window_memory: Memory = Memory.windowMemory(5)
        llm = LLM(llmConfig, window_memory)

//        Memory tokens_memory = Memory.tokenMemory(8192);
//        llm = new LLM(llmConfig,tokens_memory);
        llm!!.registerLLMCallbacks(llmCallbacks)
    }

    private fun initSDK() {
        // 初始化SDK，Appid等信息在清单中配置
        val sparkChainConfig: SparkChainConfig = SparkChainConfig.builder()
        sparkChainConfig.appID("da2f95cc")
            .apiKey("fac412ab739047c09f08d5360592cb1c")
            .apiSecret("YTNiNzQ1ZDk5ZTY1ZGJiZTQ5ODRmN2Ni") //应用申请的appid三元组
            .logLevel(0)
        val ret: Int = SparkChain.getInst().init(applicationContext, sparkChainConfig)
        if (ret == 0) {
            Log.d(TAG, "SDK初始化成功：$ret")
            showToast(this@MainActivity, "SDK初始化成功：$ret")
            setLLMConfig()
        } else {
            Log.d(TAG, "SDK初始化失败：其他错误:$ret")
            showToast(this@MainActivity, "SDK初始化失败-其他错误：$ret")
        }
    }


    private fun startChat() {
        if (llm == null) {
            Log.e(TAG, "startChat failed,please setLLMConfig before!")
            return
        }
        val usrInputText = inputText!!.text.toString()
        Log.d(TAG, "用户输入：$usrInputText")
        if (usrInputText.length >= 1) chatText!!.append("\n输入:\n    $usrInputText\n")
        val myContext = "myContext"
        val ret: Int = llm!!.arun(usrInputText, myContext)
        if (ret != 0) {
            Log.e(TAG, "SparkChain failed:\n$ret")
            return
        }
        runOnUiThread {
            inputText!!.setText("")
            chatText!!.append("输出:\n    ")
        }
        sessionFinished = false
        return
    }

    private fun unInitSDK() {
        SparkChain.getInst().unInit()
    }

    private fun initButtonClickListener() {
        startChatBtn!!.setOnClickListener {
            if (sessionFinished) {
                startChat()
                toend()
            } else {
                Toast.makeText(this@MainActivity, "Busying! Please Wait", Toast.LENGTH_SHORT).show()
            }
        }
        // 监听文本框点击时间,跳转到底部
        inputText!!.setOnClickListener { toend() }
    }

    private fun initView() {
        startChatBtn = findViewById(R.id.chat_start_btn)
        chatText = findViewById(R.id.chat_output_text)
        inputText = findViewById(R.id.chat_input_text)
        this.chatText.setMovementMethod(ScrollingMovementMethod())
        val drawable = GradientDrawable()
        // 设置圆角弧度为5dp
        drawable.cornerRadius = dp2px(this, 5f)
        // 设置边框线的粗细为1dp，颜色为黑色【#000000】
        drawable.setStroke(dp2px(this, 1f).toInt(), Color.parseColor("#000000"))
        this.inputText.setBackground(drawable)
    }

    private fun dp2px(context: Context?, dipValue: Float): Float {
        if (context == null) {
            return 0F
        }
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f)
    }

    fun showToast(context: Activity, content: String?) {
        context.runOnUiThread {
            val random = (Math.random() * (1 - 0) + 0).toInt()
            Toast.makeText(context, content, random).show()
        }
    }

    fun toend() {
        val scrollAmount = chatText!!.layout.getLineTop(chatText!!.lineCount) - chatText!!.height
        if (scrollAmount > 0) {
            chatText!!.scrollTo(0, scrollAmount + 10)
        }
    }
}

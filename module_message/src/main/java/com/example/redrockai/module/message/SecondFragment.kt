package com.example.redrockai.module.message

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.example.redrockai.module.message.databinding.FragmentSecondBinding
import com.iflytek.sparkchain.core.LLM
import com.iflytek.sparkchain.core.LLMCallbacks
import com.iflytek.sparkchain.core.LLMError
import com.iflytek.sparkchain.core.LLMEvent
import com.iflytek.sparkchain.core.LLMFactory
import com.iflytek.sparkchain.core.LLMOutput
import com.iflytek.sparkchain.core.LLMResult


class SecondFragment : Fragment(), View.OnClickListener {
    var llm: LLM? = null
    lateinit var content: String

    private var _mBinding: FragmentSecondBinding? = null
    private val mBinding: FragmentSecondBinding
        get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentSecondBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConfig()
    }


    private fun initConfig() {
        mBinding.ibSend.setOnClickListener(this)
        mBinding.ibStop.setOnClickListener(this)
        setLLMConfig()
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.ib_send) {
            if (llm != null) {
                if (mBinding.aigcFragmentAigcEt.text.isNullOrEmpty()) {
                    shortToast("输入内容不能为空哟")
                } else {
                    mBinding.ibSend.visibility = View.GONE
                    mBinding.ibStop.visibility = View.VISIBLE
                    content = mBinding.aigcFragmentAigcEt.text.toString()
                    mBinding.aigcFragmentAigcEt.text.clear()
                    clearImage()
                    showInfo("图片生成中，请稍后.....")
                    object : Thread() {
                        override fun run() { //由于同步请求后该线程会卡主，为了防止卡主线程，故开启一个线程进行同步请求
                            super.run()
                            imageGeneration_run_start()
                        }
                    }.start()
                }
            }

        } else if (id == R.id.ib_stop) {
            if (llm != null) {
                imageGeneration_stop()
                showInfo("已取消图片生成。")
                mBinding.ibSend.visibility = View.VISIBLE
                mBinding.ibStop.visibility = View.GONE
            }
        }
    }


    private fun imageGeneration_run_start() {
        //同步请求
        val syncOutput: LLMOutput = llm!!.run(content)
        val status = syncOutput.status

        if (syncOutput.errCode == 0) {

            val bytes: ByteArray? = syncOutput.image
            if (bytes != null) {
                activity?.runOnUiThread {
                    if (status == 0) {
                        mBinding.ibSend.visibility = View.VISIBLE
                        mBinding.ibStop.visibility = View.GONE
                    }
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) //把二进制图片流转换成图片
                    mBinding.imageGenerationOutputIv.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            bmp,
                            bmp.width,
                            bmp.height,
                            false
                        )
                    ) //把图片设置到对应的控件
                }
                showInfo("图片生成结束。")
            } else {
                activity?.runOnUiThread {
                    mBinding.tvResult.text = "已取消图片生成。"
                }
            }
        } else {
            activity?.runOnUiThread {
                mBinding.tvResult.text = syncOutput.errMsg
            }
        }
    }

    private fun imageGeneration_stop() {
        try {
            llm?.stop()
        } catch (e: Exception) {
            showInfo("停止图片生成时出错。")
        }
    }

    private fun showImage(bytes: ByteArray) {


    }

    private fun showInfo(text: String) {
        activity?.runOnUiThread(Runnable { mBinding.tvResult.text = text })
    }

    private fun clearImage() {
        activity?.runOnUiThread(Runnable { mBinding.imageGenerationOutputIv.setImageDrawable(null) })
    }

    private fun setLLMConfig() {
        llm = LLMFactory.imageGeneration(1024, 1024)
    }
}
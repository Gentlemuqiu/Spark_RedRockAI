package com.example.voice
import android.Manifest;
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iflytek.cloud.ErrorCode
import com.iflytek.cloud.InitListener
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechRecognizer
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "MainActivity"

    private var mIat: SpeechRecognizer? = null // 语音听写对象

    private var mIatDialog: RecognizerDialog? = null // 语音听写UI


    // 用HashMap存储听写结果
    private val mIatResults: HashMap<String, String> = LinkedHashMap()

    private var mSharedPreferences: SharedPreferences? = null //缓存


    private val mEngineType = SpeechConstant.TYPE_CLOUD // 引擎类型

    private val language = "zh_cn" //识别语言


    private lateinit var tvResult: TextView //识别结果

    private lateinit var  btnStart: Button //开始识别

    private val resultType = "json" //结果内容数据格式


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResult = findViewById(R.id.tv_result);
        btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this@MainActivity, mInitListener)
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = RecognizerDialog(this@MainActivity, mInitListener)
        mSharedPreferences = getSharedPreferences(
            "ASR",
            MODE_PRIVATE
        )
        initPermission();//权限请求

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf<String>(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val toApplyList = ArrayList<String>()
        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    this,
                    perm
                )
            ) {
                toApplyList.add(perm)
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123)
        }
    }
    override fun onClick(v: View?) {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            showMsg("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化")
            return
        }
        mIatResults.clear() //清除数据
        setParam() // 设置参数
        mIatDialog!!.setListener(mRecognizerDialogListener) //设置监听
        mIatDialog!!.show() // 显示对话框
    }


    /**
     * 初始化监听器。
     */
    private val mInitListener = InitListener { code ->
        Log.d(TAG, "SpeechRecognizer init() code = $code")
        if (code != ErrorCode.SUCCESS) {
            showMsg("初始化失败，错误码：$code,请点击网址https://www.xfyun.cn/document/error-code查询解决方案")
        }
    }


    /**
     * 听写UI监听器
     */
    private val mRecognizerDialogListener: RecognizerDialogListener =
        object : RecognizerDialogListener {
            override fun onResult(results: RecognizerResult?, isLast: Boolean) {
                if (results != null) {
                    printResult(results)
                } //结果数据解析
            }

            /**
             * 识别回调错误.
             */
            override fun onError(error: SpeechError) {
                showMsg(error.getPlainDescription(true))
            }
        }

    /**
     * 提示消息
     * @param msg
     */
    private fun showMsg(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 数据解析
     *
     * @param results
     */
    private fun printResult(results: RecognizerResult) {
        val text: String = JsonParser.parseIatResult(results.resultString)
        var sn: String? = null
        // 读取json结果中的sn字段
        try {
            val resultJson = JSONObject(results.resultString)
            sn = resultJson.optString("sn")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mIatResults[sn!!] = text
        val resultBuffer = StringBuffer()
        for (key in mIatResults.keys) {
            resultBuffer.append(mIatResults[key])
        }
        tvResult.text = resultBuffer.toString() //听写结果显示
    }

    /**
     * 参数设置
     *
     * @return
     */
    fun setParam() {
        // 清空参数
        mIat?.setParameter(SpeechConstant.PARAMS, null)
        // 设置听写引擎
        mIat?.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType)
        // 设置返回结果格式
        mIat?.setParameter(SpeechConstant.RESULT_TYPE, resultType)
        if (language == "zh_cn") {
            val lag = mSharedPreferences!!.getString(
                "iat_language_preference",
                "mandarin"
            )
            Log.e(TAG, "language:$language") // 设置语言
            mIat?.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
            // 设置语言区域
            mIat?.setParameter(SpeechConstant.ACCENT, lag)
        } else {
            mIat?.setParameter(SpeechConstant.LANGUAGE, language)
        }
        Log.e(TAG, "last language:" + mIat!!.getParameter(SpeechConstant.LANGUAGE))

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat!!.setParameter(
            SpeechConstant.VAD_BOS,
            mSharedPreferences!!.getString("iat_vadbos_preference", "4000")
        )

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat!!.setParameter(
            SpeechConstant.VAD_EOS,
            mSharedPreferences!!.getString("iat_vadeos_preference", "1000")
        )

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat!!.setParameter(
            SpeechConstant.ASR_PTT,
            mSharedPreferences!!.getString("iat_punc_preference", "1")
        )

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat!!.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        mIat!!.setParameter(
            SpeechConstant.ASR_AUDIO_PATH,
            Environment.getExternalStorageDirectory().toString() + "/msc/iat.wav"
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        if (null != mIat) {
            // 退出时释放连接
            mIat!!.cancel()
            mIat!!.destroy()
        }
    }


}
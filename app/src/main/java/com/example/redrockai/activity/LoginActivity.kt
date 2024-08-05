package com.example.redrockai.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.transition.Explode
import android.transition.TransitionManager
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.get
import com.example.module_teacher.ui.CreateActivity
import com.example.redrockai.adapter.LoginViewModel
import com.example.redrockai.databinding.ActivityLoginBinding
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.example.redrockai.lib.utils.setOnSingleClickListener
import com.example.redrockai.lib.utils.toast
import com.example.redrockai.util.SPUtils

class LoginActivity : BaseActivity() {
    private val mBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val mLottieProgress = 0.39f // 点击同意用户协议时的动画的时间


    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initView()

//        //如果已经登录的话,检测之前是否登录
//        if (SPUtils.getLoginStatus()) {
//            startActivity(Intent(this@LoginActivity, CreateActivity::class.java))
//            finish()
//        }


        // 观察登录结果
        loginViewModel.loginResult.observe(this) { response ->
            if (response.code == 0) {
                shortToast("登录成功")
                startActivity(Intent(this@LoginActivity, CreateActivity::class.java))
                finish()
            } else {
                shortToast("登录失败")
            }
        }

        mBinding.apply {
            loginBtnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

    }

    private fun initView() {
        mBinding.loginEtPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                loginAction()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        mBinding.loginBtnLogin.setOnSingleClickListener {
            loginAction()
        }
        mBinding.loginTvTouristModeEnter.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //设置用户协议和隐私政策的文字
        val spannableString = SpannableStringBuilder()
        spannableString.append("同意《用户协议》和《隐私权政策》")

        //设置用户协议和隐私权政策点击事件
        val userAgreementClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                /**设置文字颜色**/
                ds.color = ds.linkColor
                /**去除连接下划线**/
                ds.isUnderlineText = false
            }
        }
        val privacyClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                /**设置文字颜色**/
                ds.color = ds.linkColor
                /**去除连接下划线**/
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(userAgreementClickSpan, 2, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(privacyClickSpan, 9, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        //设置用户协议和隐私权政策字体颜色
        val userAgreementSpan = ForegroundColorSpan(Color.parseColor("#2CDEFF"))
        val privacySpan = ForegroundColorSpan(Color.parseColor("#2CDEFF"))
        spannableString.setSpan(userAgreementSpan, 2, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(privacySpan, 9, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }

    private fun loginAction() {
        val account = mBinding.loginEtAccount.text?.toString() ?: ""
        val password = mBinding.loginEtPassword.text?.toString() ?: ""
        if (account.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.login(account, password)
        } else {
            shortToast("账号或密码不能为空")
        }

    }


    // 这个方法可以在登录状态和未登录状态之间切换
    private fun changeUiState() {
        TransitionManager.beginDelayedTransition(mBinding.loginContainer, Explode())
        for (i in 0 until mBinding.loginContainer.childCount) {
            val view = mBinding.loginContainer[i]
            view.visibility = when (view.visibility) {
                View.GONE -> View.VISIBLE
                View.VISIBLE -> View.GONE
                else -> View.VISIBLE
            }
        }
    }

    /**
     * 同意用户协议
     */
    private fun agreeToUserAgreement() {
        toast("请先同意用户协议吧")
    }
}
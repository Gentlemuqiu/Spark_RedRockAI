package com.example.redrockai.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.transition.Explode
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import com.airbnb.lottie.LottieAnimationView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.redrockai.R
import com.example.redrockai.databinding.ActivityHomeBinding
import com.example.redrockai.databinding.ActivityLoginBinding
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.lib.utils.appContext
import com.example.redrockai.lib.utils.setOnSingleClickListener
import com.example.redrockai.lib.utils.toast

class LoginActivity : BaseActivity() {
    private val mBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val mLottieProgress = 0.39f // 点击同意用户协议时的动画的时间

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initView()

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
        mBinding.loginLavCheck.setOnSingleClickListener {
            mBinding.loginLavCheck.playAnimation()
        //    mViewModel.userAgreementIsCheck = !mViewModel.userAgreementIsCheck
        }
        mBinding.loginLavCheck.addAnimatorUpdateListener {
          /*  if (it.animatedFraction == 1f && mViewModel.userAgreementIsCheck) {
                mBinding.loginLavCheck.pauseAnimation()
            } else if (it.animatedFraction >= mLottieProgress && it.animatedFraction != 1f && !mViewModel.userAgreementIsCheck) {
                mBinding.loginLavCheck.pauseAnimation()
            }*/
        }
        //设置用户协议和隐私政策的文字
        val spannableString = SpannableStringBuilder()
        spannableString.append("同意《用户协议》和《隐私权政策》")
        //解决文字点击后变色
        mBinding.loginTvUserAgreement.highlightColor =
            ContextCompat.getColor(this, android.R.color.transparent)
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
      /*  if (mViewModel.userAgreementIsCheck) {
            //放下键盘
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            }
            val stuNum = mBinding.loginEtAccount.text?.toString() ?: ""
            val password = mBinding.loginEtPassword.text?.toString() ?: ""
            if (checkDataCorrect(stuNum, password)) {
                changeUiState()
                mViewModel.login(stuNum, password)
            }
        } else {
            agreeToUserAgreement()
        }*/
    }
    private fun checkDataCorrect(stuNum: String, idNum: String): Boolean {
        if (idNum.length < 6) {
            toast("请检查一下密码吧，似乎有点问题")
            return false
        }
        return true
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
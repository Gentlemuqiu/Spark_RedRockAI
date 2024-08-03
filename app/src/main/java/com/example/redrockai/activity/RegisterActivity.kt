package com.example.redrockai.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.redrockai.adapter.RegisterViewModel
import com.example.redrockai.databinding.ActivityRegisterBinding
import com.example.redrockai.lib.utils.BaseUtils.shortToast

class RegisterActivity : AppCompatActivity() {

    private val mBinding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initKeyBoard()
        initListener()

        // 观察注册结果
        registerViewModel.registerResult.observe(this) { response ->
            if (response.code == 0) {
                shortToast("注册成功")
//                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            } else {
                shortToast("注册失败")
            }
        }


    }

    private fun initKeyBoard() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keyboardHeight = screenHeight - rect.bottom
            val isKeyboardVisible = keyboardHeight > screenHeight * 0.15
            if (isKeyboardVisible) {
                rootView.translationY = -keyboardHeight.toFloat() / 2
            } else {
                rootView.translationY = 0f
            }


        }

    }

    private fun initListener() {

        mBinding.btnRegister.setOnClickListener {
            val account = mBinding.registerEtAccount.text.toString()
            val password = mBinding.registerEtPassword.text.toString()

            if (account.isNotEmpty() && password.isNotEmpty()) {
                // 发起注册请求
                registerViewModel.register(account, password, "https://example.com/avatar.jpg")

            } else {
                if (account.isEmpty()) shortToast("账号不能为空")
                if (password.isEmpty()) shortToast("密码不能为空")
            }
        }
    }

}
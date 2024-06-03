package com.example.module.life.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import com.example.module.life.R

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-03 19:02.
 * @Description : 生活很难，但总有人在爱你。
 */
class ReminderDialog(context: Context) : AlertDialog(context) {
    /**
     * 点击确认按钮的回调
     */
    private var mOnConfirmSelected : ((ReminderDialog) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.life_delete_if)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView(){
        findViewById<Button>(R.id.notification_dialog_btn_cancel_revoke).apply {
            setOnClickListener {
                this@ReminderDialog.cancel()
            }
        }
        findViewById<Button>(R.id.notification_dialog_btn_confirm_revoke).apply {
            setOnClickListener {
                mOnConfirmSelected?.invoke(this@ReminderDialog)
            }
        }
    }

    fun setConfirmSelected(onConfirmSelected : (ReminderDialog) -> Unit) : ReminderDialog {
        mOnConfirmSelected = onConfirmSelected
        return this
    }

    override fun cancel() {
        super.cancel()
        mOnConfirmSelected = null
    }
}
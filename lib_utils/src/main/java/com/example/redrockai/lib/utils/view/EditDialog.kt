package com.example.redrockai.lib.utils.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.redrockai.lib.utils.BaseChooseDialog
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.example.redrockai.lib.utils.R
import com.example.redrockai.lib.utils.color
import com.example.redrockai.lib.utils.dp2px

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/26 14:37
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
open class EditDialog protected constructor(
    context: Context,
) : BaseChooseDialog<EditDialog, EditDialog.DataImpl>(context) {

    open class Builder(context: Context, data: DataImpl) :
        BaseChooseDialog.Builder<EditDialog, DataImpl>(context, data) {

        override fun buildInternal(): EditDialog {
            return EditDialog(context)
        }
    }

    /**
     * @param content dialog 的文本内容
     * @param contentSize content 的字体大小
     * @param contentGravity 文本的 gravity（是 TextView 的 gravity 属性，非 layout_gravity）
     */
    open class DataImpl(
        val content: CharSequence = "弹窗内容为空",
        var hint: String? = "",
        val contentSize: Float = 13F,
        val contentGravity: Int = Gravity.CENTER,
        override val width: Int = Data.width,
        override val height: Int = Data.height,
    ) : Data by Data

    private lateinit var editText: EditText
    override fun createContentView(parent: ViewGroup): View {

        editText = EditText(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            ).apply {
                topMargin = 15.dp2px
                bottomMargin = topMargin
                leftMargin = 25.dp2px
                rightMargin = leftMargin
            }
        }
        return LinearLayout(parent.context).apply {
            orientation = LinearLayout.VERTICAL
            addView(
                TextView(parent.context).apply {
                    text = "设置你的学习时间"
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = 28.dp2px
                    }
                    setTextColor(com.example.redrockai.lib.utils.R.color.config_level_four_font_color.color)
                    textSize = 18F
                    gravity = Gravity.CENTER
                }
            )
            addView(editText)
        }


    }

    override fun initContentView(view: View) {
        editText.apply {
            hint = data.hint
            textSize = data.contentSize
            gravity = data.contentGravity
            setHintTextColor(Color.parseColor("#6615315B"))
            setTextColor(Color.parseColor("#CC15315B"))
            setBackgroundResource(R.drawable.ufield_shape_edit_shape)
            addTextChangedListener(textWatcher)
        }

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val inputText = s.toString()
            if (inputText.length > 10) {
                shortToast("已经超过十个字了哦")
                val limitedText = inputText.substring(0, 10)
                editText.setText(limitedText)
                editText.setSelection(limitedText.length)
            }
        }
    }

    fun getInput(): String {
        return editText.text.toString().trim()
    }

    fun isPositiveInteger(input: String): Boolean {
        // 定义正整数的正则表达式模式
        val pattern = "^\\+?\\d+$".toRegex()
        return pattern.matches(input)
    }


}
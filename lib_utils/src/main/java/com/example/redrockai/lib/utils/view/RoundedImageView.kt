package com.example.redrockai.lib.utils.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView

/**
 *  description : 自定义的一个imageview 上下都有圆角为
 *  author : lytMoon
 *  date : 2024/5/20
 *  email : yytds@foxmail.com
 *  version ： 1.0
 */


class RoundedImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private val path = Path()
    private val rect = RectF()

    private val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        8f,
        context.resources.displayMetrics
    )

    override fun onDraw(canvas: Canvas) {
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        path.reset()
        path.addRoundRect(
            rect,
            floatArrayOf(
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius
            ),
            Path.Direction.CW
        )
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}
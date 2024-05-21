package com.example.redrockai.lib.utils.view

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 11:21
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.LinearLayout

class RoundedLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val rect = RectF()

    init {
        // 设置画笔颜色为黑色
        paint.color = 0xFF000000.toInt()
        // 设置画笔样式为描边
        paint.style = Paint.Style.STROKE
        // 设置描边的宽度
        paint.strokeWidth = 4f
        // 设置圆角的半径
        val radius = 90f
        // 设置描边的圆角
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制圆角矩形边框
        canvas.drawPath(path, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 更新矩形的大小
        rect.set(0f, 0f, w.toFloat(), h.toFloat())
        // 重新设置圆角路径
        path.reset()
        val radius = 90f
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
    }
}

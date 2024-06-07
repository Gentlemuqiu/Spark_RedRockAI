package com.examole.redrockai.module_mine.ui.activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.examole.redrockai.module_mine.R
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.lib.utils.BaseApp
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.example.redrockai.lib.utils.SighInUtils
import com.example.redrockai.lib.utils.StudyTimeUtils
import com.example.redrockai.lib.utils.StudyTimeUtils.convertTimestampToMinutes
import com.example.redrockai.lib.utils.StudyTimeUtils.getWantedStudiedTime
import com.example.redrockai.lib.utils.getStringList
import com.example.redrockai.lib.utils.putStringList
import com.example.redrockai.lib.utils.view.EditDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.coroutines.launch

class SignInActivity : BaseActivity() {
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var signInButton: Button
    private lateinit var mEdit: ImageView
    private lateinit var mBack: ImageView


    private val signedDates = mutableSetOf<CalendarDay>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sigh_in)
        shortToast("学习时间超过设定时间便会自动打卡成功呦~")
        initBindView()
        readSignedDates()
        initListener()

        // 应用已签到日期的装饰
        calendarView.addDecorators(SignInDecorator())

        //处理签到按钮的点击事件
//        signInButton.setOnClickListener {
//            val today = CalendarDay.today()
//            if (signedDates.add(today)) {
//                // 保存已签到日期
//                saveSignedDates()
//                calendarView.invalidateDecorators()
//                shortToast("打卡成功")
//            } else {
//                shortToast("今天已经打卡过了")
//            }
//        }


    }

    private fun initListener() {
        //当学习的时间大于设置的时间的时候自动打卡
        lifecycleScope.launch {
            StudyTimeUtils.studiedTime.collect {
                val today = CalendarDay.today()
                if (convertTimestampToMinutes(it) >= getWantedStudiedTime().toLong() && SighInUtils.haveToast == false) {
                    //签到成功
                    shortToast("恭喜您打卡成功")
                    SighInUtils.haveToast = true
                    if (signedDates.add(today)) {
                        // 保存已签到日期
                        saveSignedDates()
                        calendarView.invalidateDecorators()
                    } else {
                        shortToast("今天已经签到过了")
                    }
                }
            }
        }



        mEdit.setOnClickListener {
            EditDialog.Builder(
                this,
                EditDialog.DataImpl(
                    content = "",
                    hint = "分钟",
                    width = 255,
                    height = 207
                )
            ).setPositiveClick {
                //正整数
                if (isPositiveInteger(getInput())) {
                    StudyTimeUtils.saveWantedStudiedTime(getInput())
                    shortToast("设置成功")
                    dismiss()
                } else {
                    shortToast("输入不合理,请输入正整数时间")
                }

            }.setNegativeClick {
                dismiss()
            }.show()

        }

    }

    private fun initBindView() {
        calendarView = findViewById(R.id.calendarView)
//        signInButton = findViewById(R.id.signInButton)
        mEdit = findViewById(R.id.iv_edit)
        mBack = findViewById<ImageView?>(R.id.iv_back).apply {
            setOnClickListener {
                finish()
            }
        }
    }


    //保存已经签到的日子
    private fun saveSignedDates() {
        val sharedPreferences = getSharedPreferences("SignInPrefs", Context.MODE_PRIVATE)
        val dateList = signedDates.map { "${it.year}-${it.month}-${it.day}" }
        sharedPreferences.putStringList("signed_dates", dateList)
    }

    //读取已经签到的日子
    private fun readSignedDates() {
        val sharedPreferences = getSharedPreferences("SignInPrefs", Context.MODE_PRIVATE)
        val dateList = sharedPreferences.getStringList("signed_dates")
        dateList.forEach { dateString ->
            val parts = dateString.split("-")
            if (parts.size == 3) {
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                val day = parts[2].toInt()
                signedDates.add(CalendarDay.from(year, month, day))
            }
        }

    }

    inner class SignInDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return signedDates.contains(day)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(
                DotSpan(
                    8f,
                    getColor(com.example.redrockai.lib.utils.R.color.config_calendar_selected)
                )
            )
            val drawable: Drawable? = ContextCompat.getDrawable(
                BaseApp.getAppContext(),
                R.drawable.custom_signin_background
            )
            drawable?.let {
                view?.setBackgroundDrawable(it)
            }
        }
    }


}
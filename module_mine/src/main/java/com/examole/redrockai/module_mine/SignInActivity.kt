package com.examole.redrockai.module_mine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class SignInActivity : AppCompatActivity() {
    private lateinit var calendarView: MaterialCalendarView
    private val signedDates = HashSet<CalendarDay>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sigh_in)


    }



}
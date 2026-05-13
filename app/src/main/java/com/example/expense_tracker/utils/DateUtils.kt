package com.example.expense_tracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val displayDateFmt = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    private val fullDateTimeFmt = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.ENGLISH)
    private val groupHeaderFmt = SimpleDateFormat("MMM dd", Locale.ENGLISH)
    private val monthYearFmt = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val shortMonthFmt = SimpleDateFormat("MMM", Locale.ENGLISH)
    private val timeFmt = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    fun Long.toDisplayDate(): String = displayDateFmt.format(Date(this))
    fun Long.toFullDateTime(): String = fullDateTimeFmt.format(Date(this))
    fun Long.toTimeString(): String = timeFmt.format(Date(this))
    fun Long.toShortDate(): String = groupHeaderFmt.format(Date(this))

    fun Long.toGroupHeader(): String {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = this@toGroupHeader }

        val sameYear = target.get(Calendar.YEAR) == now.get(Calendar.YEAR)
        val targetDay = target.get(Calendar.DAY_OF_YEAR)
        val todayDay = now.get(Calendar.DAY_OF_YEAR)

        return when {
            sameYear && targetDay == todayDay -> "Today"
            sameYear && targetDay == todayDay - 1 -> "Yesterday"
            else -> groupHeaderFmt.format(Date(this))
        }
    }

    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    fun toMonthYearLabel(month: Int, year: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
        }
        return monthYearFmt.format(cal.time)
    }

    fun toShortMonthLabel(month: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
        }
        return shortMonthFmt.format(cal.time)
    }

    fun startOfToday(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    fun monthRangeMs(month: Int, year: Int): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            set(year, month - 1, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val end = Calendar.getInstance().apply {
            set(year, month - 1, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MONTH, 1)
        }.timeInMillis - 1L

        return start to end
    }

    fun lastNMonths(n: Int): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        val cal = Calendar.getInstance()
        repeat(n) {
            result.add(cal.get(Calendar.MONTH) + 1 to cal.get(Calendar.YEAR))
            cal.add(Calendar.MONTH, -1)
        }
        return result
    }
}
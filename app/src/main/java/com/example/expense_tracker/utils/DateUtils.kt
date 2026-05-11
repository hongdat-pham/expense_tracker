package com.example.expense_tracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Shared utility — used by Thành (Transaction), Tiến Đạt (Overview/Activity), Quỳnh (Analytics).
 * Push to dev branch immediately after writing.
 *
 * All functions are extension functions on Long (millisecond timestamps).
 */
object DateUtils {

    private val displayDateFmt = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    private val fullDateTimeFmt = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.ENGLISH)
    private val monthYearFmt   = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
    private val shortMonthFmt  = SimpleDateFormat("MMM", Locale.ENGLISH)
    private val groupHeaderFmt = SimpleDateFormat("MMM dd", Locale.ENGLISH)

    /**
     * "Oct 24, 2023" — used in Transaction Detail and date pickers
     */
    fun Long.toDisplayDate(): String = displayDateFmt.format(Date(this))

    /**
     * "Oct 03, 2023, 14:30" — used in Activity Detail hero section
     */
    fun Long.toFullDateTime(): String = fullDateTimeFmt.format(Date(this))

    /**
     * "Today" / "Yesterday" / "Nov 21" — used as section headers in Activity list
     */
    fun Long.toGroupHeader(): String {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = this@toGroupHeader }

        val todayYear  = now.get(Calendar.YEAR)
        val todayDay   = now.get(Calendar.DAY_OF_YEAR)
        val targetYear = target.get(Calendar.YEAR)
        val targetDay  = target.get(Calendar.DAY_OF_YEAR)

        return when {
            targetYear == todayYear && targetDay == todayDay     -> "Today"
            targetYear == todayYear && targetDay == todayDay - 1 -> "Yesterday"
            else -> groupHeaderFmt.format(Date(this))            // "Nov 21"
        }
    }

    /**
     * "Oct 2023" — used in Analytics month filter
     */
    fun Long.toMonthYear(): String = monthYearFmt.format(Date(this))

    /**
     * Short month abbreviation — "Jan", "Feb", … — used as BarChart X-axis labels
     */
    fun Long.toShortMonth(): String = shortMonthFmt.format(Date(this))

    /**
     * Current month as 1–12 integer
     */
    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    /**
     * Current year as 4-digit integer
     */
    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    /**
     * Start of today (00:00:00.000) as millisecond timestamp
     */
    fun startOfToday(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    /**
     * Returns a list of (month, year) pairs for the last N months including current month.
     * e.g. last6Months() → [(5,2025),(4,2025),(3,2025),(2,2025),(1,2025),(12,2024)]
     * Used by Analytics BarChart/LineChart.
     */
    fun lastNMonths(n: Int): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        val cal = Calendar.getInstance()
        repeat(n) {
            result.add(Pair(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)))
            cal.add(Calendar.MONTH, -1)
        }
        return result
    }
}
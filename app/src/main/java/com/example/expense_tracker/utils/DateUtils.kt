package com.example.expense_tracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Shared utility — used by Thành (Transaction), Tiến Đạt (Overview/Activity), Quỳnh (Analytics).
 * Push to dev branch immediately after writing.
 */
object DateUtils {

    private val displayDateFmt  = SimpleDateFormat("MMM dd, yyyy",        Locale.ENGLISH)
    private val fullDateTimeFmt = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.ENGLISH)
    private val groupHeaderFmt  = SimpleDateFormat("MMM dd",               Locale.ENGLISH)
    private val monthYearFmt    = SimpleDateFormat("MMMM yyyy",            Locale.ENGLISH)
    private val shortMonthFmt   = SimpleDateFormat("MMM",                  Locale.ENGLISH)

    // ── Extension functions on Long (timestamp ms) ────────────────────────────

    /** "Oct 24, 2023" — used in Transaction Detail and date pickers */
    fun Long.toDisplayDate(): String = displayDateFmt.format(Date(this))

    /** "Oct 03, 2023, 14:30" — used in Activity Detail hero section */
    fun Long.toFullDateTime(): String = fullDateTimeFmt.format(Date(this))

    /**
     * "Today" / "Yesterday" / "Nov 21" — used as section headers in Activity list.
     */
    fun Long.toGroupHeader(): String {
        val now    = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = this@toGroupHeader }

        val sameYear  = target.get(Calendar.YEAR) == now.get(Calendar.YEAR)
        val targetDay = target.get(Calendar.DAY_OF_YEAR)
        val todayDay  = now.get(Calendar.DAY_OF_YEAR)

        return when {
            sameYear && targetDay == todayDay     -> "Today"
            sameYear && targetDay == todayDay - 1 -> "Yesterday"
            else -> groupHeaderFmt.format(Date(this))   // "Nov 21"
        }
    }

    // ── Static helpers (work with month/year integers, not timestamps) ─────────

    /** Current month as 1–12 integer */
    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    /** Current year as 4-digit integer */
    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    /**
     * "October 2024" — used in Analytics month navigator header.
     * @param month  1-based (1 = January)
     */
    fun toMonthYearLabel(month: Int, year: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
        }
        return monthYearFmt.format(cal.time)
    }

    /**
     * "Jan", "Feb", … — used as BarChart X-axis labels.
     * @param month  1-based
     */
    fun toShortMonthLabel(month: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
        }
        return shortMonthFmt.format(cal.time)
    }

    /**
     * Start of today (00:00:00.000) as millisecond timestamp.
     */
    fun startOfToday(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE,      0)
        set(Calendar.SECOND,      0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    /**
     * Returns (startMs, endMs) inclusive range for a given month/year.
     * Used by Analytics ViewModel and OverviewViewModel to query by month.
     * @param month  1-based
     */
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

    /**
     * Returns a list of (month, year) pairs for the last N months including current,
     * ordered newest → oldest.
     * e.g. lastNMonths(6) → [(5,2025),(4,2025),(3,2025),(2,2025),(1,2025),(12,2024)]
     */
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
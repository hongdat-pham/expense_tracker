package com.example.expense_tracker.utils

import com.example.expense_tracker.data.local.entity.TransactionType
import java.text.NumberFormat
import java.util.Locale

/**
 * Shared utility — used by Thành (Transaction), Tiến Đạt (Overview/Activity), Quỳnh (Analytics).
 * Push to dev branch immediately after writing.
 */
object CurrencyFormatter {

    private val formatter: NumberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    /**
     * Format a raw positive amount as a display string.
     * e.g. 1250.0 → "$1,250.00"
     */
    fun formatAmount(amount: Double): String = "$${formatter.format(amount)}"

    /**
     * Format with a +/- sign prefix based on transaction type.
     * e.g. EXPENSE 142.30 → "-$142.30"  |  INCOME 1250.0 → "+$1,250.00"
     *
     * UI should also color EXPENSE red (#ba1a1a) and INCOME green (#005212).
     */
    fun formatWithSign(amount: Double, type: TransactionType): String {
        val base = formatAmount(amount)
        return if (type == TransactionType.EXPENSE) "-$base" else "+$base"
    }

    /**
     * Format as a compact value for small UI elements (e.g. chart labels).
     * e.g. 1250.0 → "$1.3K"  |  500.0 → "$500"
     */
    fun formatCompact(amount: Double): String {
        return when {
            amount >= 1_000_000 -> "$${String.format("%.1fM", amount / 1_000_000)}"
            amount >= 1_000    -> "$${String.format("%.1fK", amount / 1_000)}"
            else               -> formatAmount(amount)
        }
    }
}
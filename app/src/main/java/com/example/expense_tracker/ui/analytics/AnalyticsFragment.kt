package com.example.expense_tracker.ui.analytics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.R
import com.example.expense_tracker.databinding.FragmentAnalyticsBinding
import com.example.expense_tracker.utils.CurrencyFormatter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    // Simple ViewModel factory — replace with Hilt / ViewModelProvider.Factory as needed
    private val viewModel: AnalyticsViewModel by viewModels {
        AnalyticsViewModelFactory(requireContext())
    }

    // Adapters
    private lateinit var legendAdapter: PieLegendAdapter
    private lateinit var intensityAdapter: IntensityAdapter

    // Chart color palette matching design system
    private val chartColors by lazy {
        listOf(
            parseColor("#004d64"),  // primary
            parseColor("#87d0f2"),  // primary-fixed-dim
            parseColor("#d5e5ef"),  // secondary-fixed
            parseColor("#a3f69c"),  // tertiary-fixed
            parseColor("#dfe3e8"),  // surface-variant
            parseColor("#006684"),  // primary-container
            parseColor("#bee9ff"),  // primary-fixed
        )
    }

    // ── Lifecycle ────────────────────────────────────────────────────────────

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        setupRecyclerViews()
        setupMonthNavigation()
        observeUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ── Setup ────────────────────────────────────────────────────────────────

    private fun setupCharts() {
        setupPieChart()
        setupBarChart()
    }

    private fun setupPieChart() {
        binding.pieChart.apply {
            setHoleRadius(60f)
            setTransparentCircleRadius(65f)
            setTransparentCircleAlpha(30)
            holeColor = Color.TRANSPARENT
            setDrawCenterText(true)
            setCenterTextColor(parseColor("#181c20"))
            setCenterTextSize(14f)
            description.isEnabled = false
            legend.isEnabled = false           // We draw our own legend
            isRotationEnabled = true
            setEntryLabelColor(Color.TRANSPARENT)  // Hide slice labels
            setEntryLabelTextSize(0f)
            setNoDataText("No spending this month")
            setNoDataTextColor(parseColor("#3f484d"))
        }
    }

    private fun setupBarChart() {
        binding.barChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            isDoubleTapToZoomEnabled = false
            setPinchZoom(false)
            setScaleEnabled(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                setDrawAxisLine(false)
                textColor = parseColor("#3f484d")
                textSize = 10f
                granularity = 1f
            }
            axisLeft.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                setDrawLabels(false)
            }
            axisRight.isEnabled = false

            setNoDataText("No data available")
            setNoDataTextColor(parseColor("#3f484d"))
        }
    }

    private fun setupRecyclerViews() {
        legendAdapter = PieLegendAdapter()
        binding.rvPieLegend.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = legendAdapter
            isNestedScrollingEnabled = false
        }

        intensityAdapter = IntensityAdapter()
        binding.rvIntensity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = intensityAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupMonthNavigation() {
        binding.btnPrevMonth.setOnClickListener { viewModel.previousMonth() }
        binding.btnNextMonth.setOnClickListener { viewModel.nextMonth() }
    }

    // ── Observers ────────────────────────────────────────────────────────────

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.isLoading) return@collect

                    binding.tvMonthLabel.text = state.monthLabel
                    updatePieChart(state)
                    updateKeyInsights(state)
                    updateBarChart(state)
                    updateIntensityReport(state)
                }
            }
        }
    }

    // ── Chart update helpers ─────────────────────────────────────────────────

    private fun updatePieChart(state: AnalyticsUiState) {
        if (state.categorySpending.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.invalidate()
            binding.tvTrendPercent.text = "No data"
            legendAdapter.submitList(emptyList())
            return
        }

        // Build PieEntries
        val entries = state.categorySpending.map { cs ->
            PieEntry(cs.percent, cs.category.displayName)
        }

        val dataSet = PieDataSet(entries, "").apply {
            colors = chartColors.take(entries.size)
                .map { it }
                .toMutableList()
                .apply {
                    // Pad with repeating colors if more categories than palette
                    while (size < entries.size) add(chartColors[size % chartColors.size])
                }
            sliceSpace = 3f
            selectionShift = 6f
            setDrawValues(false)
        }

        binding.pieChart.apply {
            data = PieData(dataSet)
            // Center text: total spent
            centerText = CurrencyFormatter.formatAmount(state.totalSpent)
            animateY(800, Easing.EaseInOutQuad)
            invalidate()
        }

        // Trend label
        val trendText = when {
            state.trendPercent < 0 -> "%.1f%% vs last month".format(-state.trendPercent)
            state.trendPercent > 0 -> "+%.1f%% vs last month".format(state.trendPercent)
            else -> "Same as last month"
        }
        binding.tvTrendPercent.text = trendText
        val trendColor = if (state.trendPercent <= 0) R.color.tertiary else R.color.error
        binding.tvTrendPercent.setTextColor(ContextCompat.getColor(requireContext(), trendColor))
        binding.ivTrendIcon.setImageResource(
            if (state.trendPercent <= 0) R.drawable.ic_arrow_downward else R.drawable.ic_arrow_upward
        )
        binding.ivTrendIcon.imageTintList =
            ContextCompat.getColorStateList(requireContext(), trendColor)

        // Custom legend
        val legendItems = state.categorySpending.mapIndexed { i, cs ->
            PieLegendItem(
                color   = chartColors.getOrElse(i) { chartColors.last() },
                label   = cs.category.displayName,
                percent = "%.0f%%".format(cs.percent),
                amount  = CurrencyFormatter.formatAmount(cs.amount)
            )
        }
        legendAdapter.submitList(legendItems)
    }

    private fun updateKeyInsights(state: AnalyticsUiState) {
        // Highest spend category
        val highest = state.highestCategory
        if (highest != null) {
            binding.tvHighestCategoryName.text = highest.category.displayName
            binding.tvHighestCategoryPercent.text = "%.0f%% of total".format(highest.percent)
            // Set icon using Material Symbol name is not directly possible in XML without
            // a mapping; for now we use a generic icon. Teams can extend this with a
            // categoryId → drawable map in Constants.
            binding.ivHighestCategoryIcon.setImageResource(
                getCategoryIconRes(highest.category.icon)
            )
        }

        // Estimated savings
        val savingsColor = if (state.estimatedSavings >= 0) R.color.tertiary else R.color.error
        binding.tvEstSavings.text = CurrencyFormatter.formatAmount(
            kotlin.math.abs(state.estimatedSavings)
        )
        binding.tvEstSavings.setTextColor(
            ContextCompat.getColor(requireContext(), savingsColor)
        )
    }

    private fun updateBarChart(state: AnalyticsUiState) {
        if (state.monthlyTotals.isEmpty()) {
            binding.barChart.clear()
            return
        }

        val entries = state.monthlyTotals.mapIndexed { i, mt ->
            BarEntry(i.toFloat(), mt.expense.toFloat())
        }
        val labels = state.monthlyTotals.map { it.label }

        // Highlight the current month (last entry)
        val primaryColor   = parseColor("#004d64")
        val inactiveColor  = parseColor("#1A004D64")   // primary at ~10% opacity

        val barColors = entries.indices.map { i ->
            if (i == entries.lastIndex) primaryColor else inactiveColor
        }

        val dataSet = BarDataSet(entries, "").apply {
            colors = barColors
            setDrawValues(false)
        }

        binding.barChart.apply {
            data = BarData(dataSet).apply { barWidth = 0.6f }
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.labelCount = labels.size
            animateY(600)
            invalidate()
        }

        // Avg label
        val avg = state.avgMonthlyExpense
        binding.tvBarChartAvg.text = "Avg: ${CurrencyFormatter.formatAmount(avg)}"
    }

    private fun updateIntensityReport(state: AnalyticsUiState) {
        val isEmpty = state.intensityRows.isEmpty()
        binding.layoutIntensityEmpty.isVisible = isEmpty
        binding.rvIntensity.isVisible = !isEmpty
        if (!isEmpty) {
            intensityAdapter.submitList(state.intensityRows)
        }
    }

    // ── Utility ──────────────────────────────────────────────────────────────

    private fun parseColor(hex: String): Int = Color.parseColor(hex)

    /**
     * Maps a Material Symbol name (stored as a string in Category) to a drawable resource.
     * Add more entries as your categories grow.
     */
    private fun getCategoryIconRes(iconName: String): Int = when (iconName) {
        "restaurant"             -> R.drawable.ic_restaurant
        "local_grocery_store"    -> R.drawable.ic_grocery
        "directions_car"         -> R.drawable.ic_car
        "shopping_bag"           -> R.drawable.ic_shopping_bag
        "home"                   -> R.drawable.ic_home
        "movie"                  -> R.drawable.ic_movie
        "health_and_safety"      -> R.drawable.ic_health
        "school"                 -> R.drawable.ic_school
        "bolt"                   -> R.drawable.ic_bolt
        "flight"                 -> R.drawable.ic_flight
        "spa"                    -> R.drawable.ic_spa
        "pets"                   -> R.drawable.ic_pets
        "account_balance_wallet" -> R.drawable.ic_wallet
        "laptop_mac"             -> R.drawable.ic_laptop
        "trending_up"            -> R.drawable.ic_trending_up
        "card_giftcard"          -> R.drawable.ic_gift
        "payments"               -> R.drawable.ic_payments
        else                     -> R.drawable.ic_more_horiz
    }
}

// ── PieLegendAdapter ─────────────────────────────────────────────────────────

data class PieLegendItem(
    val color: Int,
    val label: String,
    val percent: String,
    val amount: String
)

class PieLegendAdapter : RecyclerView.Adapter<PieLegendAdapter.VH>() {

    private var items: List<PieLegendItem> = emptyList()

    fun submitList(list: List<PieLegendItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_legend, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dot: View     = itemView.findViewById(R.id.viewDot)
        private val label: TextView = itemView.findViewById(R.id.tvLegendLabel)
        private val percent: TextView = itemView.findViewById(R.id.tvLegendPercent)
        private val amount: TextView = itemView.findViewById(R.id.tvLegendAmount)

        fun bind(item: PieLegendItem) {
            dot.backgroundTintList =
                android.content.res.ColorStateList.valueOf(item.color)
            label.text   = item.label
            percent.text = item.percent
            amount.text  = item.amount
        }
    }
}

// ── IntensityAdapter ─────────────────────────────────────────────────────────

class IntensityAdapter : RecyclerView.Adapter<IntensityAdapter.VH>() {

    private var items: List<IntensityRow> = emptyList()

    fun submitList(list: List<IntensityRow>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_intensity_report, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        private val name: TextView  = itemView.findViewById(R.id.tvIntensityCategoryName)
        private val pct: TextView   = itemView.findViewById(R.id.tvIntensityPercent)
        private val amt: TextView   = itemView.findViewById(R.id.tvIntensityAmount)
        private val bar: View       = itemView.findViewById(R.id.viewProgressFill)

        fun bind(row: IntensityRow) {
            name.text = row.category.displayName
            pct.text  = "%.0f%% of total".format(row.percent)
            amt.text  = CurrencyFormatter.formatAmount(row.amount)

            // Scale the mini progress bar based on percent (max width 80dp)
            val maxWidthPx = itemView.context.resources
                .getDimensionPixelSize(R.dimen.intensity_bar_max_width)   // 80dp in dimens.xml
            val params = bar.layoutParams
            params.width = ((row.percent / 100f) * maxWidthPx).toInt().coerceAtLeast(4)
            bar.layoutParams = params

            // Color: red if >80%, primary otherwise
            val colorRes = if (row.percent >= 80f) R.color.error else R.color.primary
            bar.backgroundTintList = android.content.res.ColorStateList.valueOf(
                androidx.core.content.ContextCompat.getColor(itemView.context, colorRes)
            )
        }
    }
}
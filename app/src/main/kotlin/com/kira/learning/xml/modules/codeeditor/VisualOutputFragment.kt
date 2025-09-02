package com.kira.learning.xml.modules.codeeditor

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.kira.learning.R

class VisualOutputFragment : Fragment() {

    private lateinit var chartContainer: FrameLayout
    private lateinit var placeholder: TextView
    private lateinit var resultSummary: TextView

    private var lineChart: LineChart? = null
    private val dataEntries = ArrayList<Entry>()
    private var maxPoints = 50

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_visual_output, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartContainer = view.findViewById(R.id.chart_container)
        placeholder = view.findViewById(R.id.visual_placeholder)
        resultSummary = view.findViewById(R.id.result_summary)

        // 初始状态显示占位符
        showPlaceholder(true)
    }

    fun showPlaceholder(show: Boolean) {
        activity?.runOnUiThread {
            if (show) {
                placeholder.visibility = View.VISIBLE
                chartContainer.visibility = View.GONE
            } else {
                placeholder.visibility = View.GONE
                chartContainer.visibility = View.VISIBLE
            }
        }
    }

    fun displayChart() {
        activity?.runOnUiThread {
            // 初始化图表
            if (lineChart == null) {
                lineChart = LineChart(requireContext()).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(Color.BLACK)

                    // 配置描述和图例
                    description.isEnabled = false
                    legend.apply {
                        textColor = Color.WHITE
                        verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                        horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                        orientation = Legend.LegendOrientation.HORIZONTAL
//                        drawInside = false
                        formSize = 12f
                    }

                    // 配置X轴
                    xAxis.apply {
                        textColor = Color.LTGRAY
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        axisLineColor = Color.DKGRAY
                        granularity = 1f
//                        valueFormatter = { value, _ ->
//                            if (value >= 0) "Step ${value.toInt() + 1}" else ""
//                        }
                    }

                    // 配置左侧Y轴
                    axisLeft.apply {
                        textColor = Color.LTGRAY
                        axisLineColor = Color.DKGRAY
                        setDrawGridLines(true)
                        gridColor = Color.DKGRAY
                        granularity = 0.5f
                    }

                    // 隐藏右侧Y轴
                    axisRight.isEnabled = false

                    // 添加图表到容器
                    chartContainer.removeAllViews()
                    chartContainer.addView(this)
                }
            }

            // 显示图表
            showPlaceholder(false)
        }
    }

    fun addDataPoint(value: Float, step: Int = dataEntries.size) {
        activity?.runOnUiThread {
            // 添加数据点
            dataEntries.add(Entry(step.toFloat(), value))

            // 限制最大点数
            if (dataEntries.size > maxPoints) {
                dataEntries.removeAt(0)
                // 更新所有点的X值
                for (i in dataEntries.indices) {
                    dataEntries[i] = Entry(i.toFloat(), dataEntries[i].y)
                }
            }

            // 创建数据集
            val dataSet = LineDataSet(dataEntries, "计算结果").apply {
                color = Color.GREEN
                valueTextColor = Color.WHITE
                lineWidth = 2f
                circleRadius = 4f
                circleHoleRadius = 2f
                setCircleColor(Color.GREEN)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.1f
                valueTextSize = 10f
            }

            // 更新图表数据
            lineChart?.data = LineData(dataSet)
            lineChart?.invalidate()
            lineChart?.zoomOut()
        }
    }

    fun setResultSummary(result: String) {
        activity?.runOnUiThread {
            resultSummary.text = "计算结果: $result"
        }
    }

    fun clearVisualization() {
        activity?.runOnUiThread {
            dataEntries.clear()
            lineChart?.clear()
            lineChart?.invalidate()
            resultSummary.text = ""
            showPlaceholder(true)
        }
    }
}
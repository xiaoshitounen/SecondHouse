package swu.xl.secondhouse.ui.fragment

import android.view.View
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_analysis.*
import kotlinx.android.synthetic.main.fragment_analysis.tab
import kotlinx.android.synthetic.main.fragment_house.*
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseFragment
import swu.xl.secondhouse.log.SecondHouseLogger
import swu.xl.secondhouse.manager.CityManager
import swu.xl.secondhouse.manager.OnCityChangeListener
import swu.xl.secondhouse.model.HouseSeal
import swu.xl.secondhouse.model.HouseUpDown
import swu.xl.secondhouse.model.Index
import swu.xl.secondhouse.util.*
import java.io.IOException

class AnalysisFragment : BaseFragment() {
    private val title = listOf<String>("近一年调价房源数量图", "在售房源数量图", "销售价格环比指数图", "销售价格同比指数图")
    private val chart = listOf<() -> Unit>(
        { loadHouseUpDownChart() },
        { loadHouseSealChart() },
        { loadHouseHBiIndexChart() },
        { loadHouseTBiIndexChart() }
    )

    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_analysis, null)
    }

    override fun initData() {
        title.forEachIndexed { index, title ->
            val item = tab.newTab().setText(title)
            item.tag = chart[index]
            tab.addTab(item)
        }

        loadDefault()
    }

    override fun initListener() {
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p: TabLayout.Tab) { }

            override fun onTabUnselected(p: TabLayout.Tab) { }

            override fun onTabSelected(p: TabLayout.Tab) {
                (p.tag as () -> Unit).invoke()
            }
        })

        CityManager.registerOnCityChangeListener(object : OnCityChangeListener {
            override fun onCityChange(city: String) {
                reLoad()
            }
        })
    }

    private fun loadDefault() {
        loadHouseUpDownChart()
    }

    private fun reLoad() {
        tab?.getTabAt(0)?.select()
        loadDefault()
    }

    private fun loadHouseUpDownChart() {
        val url = URLProviderUtil.getHouseUpDownURL()
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("调价房源数量查询失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    val houseUpDown = GsonUtil.getInstance().fromJson(it.string(), HouseUpDown::class.java).data

                    ThreadUtil.runOnUiThread {
                        val axisValues = mutableListOf<AxisValue>()
                        val downPointValues = mutableListOf<PointValue>()
                        val upPointValues = mutableListOf<PointValue>()

                        houseUpDown.forEachIndexed { index, item ->
                            axisValues.add(AxisValue(index.toFloat()).setLabel(item.Date))
                            upPointValues.add(PointValue(index.toFloat(), item.Lianjia_up.toFloat()))
                            downPointValues.add(PointValue(index.toFloat(), item.Lianjia_down.toFloat()))
                        }

                        initChart(downPointValues, upPointValues, axisValues, 7, 5)
                    }
                }
            }
        })
    }

    private fun loadHouseSealChart() {
        val url = URLProviderUtil.getHouseSealURL()
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("在售房源数量查询失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    val houseSeal = GsonUtil.getInstance().fromJson(it.string(), HouseSeal::class.java).data

                    ThreadUtil.runOnUiThread {
                        val axisValues = mutableListOf<AxisValue>()
                        val pointValues = mutableListOf<PointValue>()

                        houseSeal.forEachIndexed { index, item ->
                            axisValues.add(AxisValue(index.toFloat()).setLabel(item.Date))
                            pointValues.add(PointValue(index.toFloat(), item.Lianjia.toFloat()))
                        }

                        initChart(pointValues, emptyList(), axisValues, 9, 6)
                    }
                }
            }
        })
    }

    private fun loadHouseHBiIndexChart() {
        val url = URLProviderUtil.getHouseIndexURL()
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("价格环比上月查询失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    val index = GsonUtil.getInstance().fromJson(it.string(), Index::class.java).data

                    ThreadUtil.runOnUiThread {
                        val axisValues = mutableListOf<AxisValue>()
                        val pointValues = mutableListOf<PointValue>()

                        index.forEachIndexed { index, item ->
                            axisValues.add(AxisValue(index.toFloat()).setLabel(item.Time))
                            pointValues.add(PointValue(index.toFloat(), item.Ehuanbi.toFloat()))
                        }

                        initChart(pointValues, emptyList(), axisValues, 7, 5)
                    }
                }
            }
        })
    }

    private fun loadHouseTBiIndexChart() {
        val url = URLProviderUtil.getHouseIndexURL()
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("价格同比上月查询失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    val index = GsonUtil.getInstance().fromJson(it.string(), Index::class.java).data

                    ThreadUtil.runOnUiThread {
                        val axisValues = mutableListOf<AxisValue>()
                        val pointValues = mutableListOf<PointValue>()

                        index.forEachIndexed { index, item ->
                            axisValues.add(AxisValue(index.toFloat()).setLabel(item.Time))
                            pointValues.add(PointValue(index.toFloat(), item.Etongbi.toFloat()))
                        }

                        initChart(pointValues, emptyList(), axisValues, 7, 3)
                    }
                }
            }
        })
    }

    private fun initChart(firstValues: List<PointValue>, secondValues: List<PointValue>, axis: List<AxisValue>, xNum: Int, yNum: Int) {
        if (line_chart == null || pre_line_chart == null) return

        val lines = mutableListOf<Line>()

        if (firstValues.isNotEmpty()) {
            val firstLine = Line(firstValues)
            firstLine.color = ChartUtils.COLOR_GREEN
            firstLine.setShape(ValueShape.CIRCLE)
                .setCubic(true)
                .setFilled(true)
                .setHasLabelsOnlyForSelected(true)
            lines.add(firstLine)
        }

        if (secondValues.isNotEmpty()) {
            val secondLine = Line(secondValues)
            secondLine.color = ChartUtils.COLOR_BLUE
            secondLine.setShape(ValueShape.CIRCLE)
                .setCubic(true)
                .setFilled(true)
                .setHasLabelsOnlyForSelected(true)
            lines.add(secondLine)
        }

        val chartData = LineChartData(lines)
        chartData.axisXBottom = Axis().apply {
            values = axis
            maxLabelChars = xNum
            textSize = 10
        }
        chartData.axisYLeft = Axis().apply {
            maxLabelChars = yNum
            textSize = 10
        }

        val preChartData = LineChartData(chartData)
        preChartData.lines[0].color = ChartUtils.DEFAULT_DARKEN_COLOR

        line_chart.lineChartData = chartData
        pre_line_chart.lineChartData = preChartData

        line_chart.isZoomEnabled = false
        line_chart.isScrollEnabled = false

        val tempViewport = Viewport(line_chart.maximumViewport)
        val dx = tempViewport.width() / 3
        tempViewport.inset(dx, 0f)
        pre_line_chart.setCurrentViewportWithAnimation(tempViewport)
        pre_line_chart.zoomType = ZoomType.HORIZONTAL

        pre_line_chart.setViewportChangeListener { port ->
            line_chart?.currentViewport = port
        }
    }
}
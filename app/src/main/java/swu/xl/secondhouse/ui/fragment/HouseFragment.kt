package swu.xl.secondhouse.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_home.list
import kotlinx.android.synthetic.main.fragment_house.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import swu.xl.secondhouse.R
import swu.xl.secondhouse.adapter.HouseAdapter
import swu.xl.secondhouse.base.BaseFragment
import swu.xl.secondhouse.log.SecondHouseLogger
import swu.xl.secondhouse.manager.CityManager
import swu.xl.secondhouse.manager.OnCityChangeListener
import swu.xl.secondhouse.model.Detail
import swu.xl.secondhouse.model.House
import swu.xl.secondhouse.util.*
import java.io.IOException

class HouseFragment : BaseFragment() {
    private var area: String? = null
    private val adapter by lazy { HouseAdapter() }

    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_house, null)
    }

    override fun initData() {
        loadArea()
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        refresh.setEnableOverScrollBounce(true)
    }

    override fun initListener() {
        refresh.setOnRefreshListener { loadData() }
        refresh.setOnLoadMoreListener { loadMore(adapter.itemCount) }

        CityManager.registerOnCityChangeListener(object : OnCityChangeListener {
            override fun onCityChange(city: String) {
                try {
                    tab.removeAllTabs()
                    loadArea()
                } catch (e: Exception) {
                    SecondHouseLogger.e("tab is null")
                }
            }
        })
    }

    private fun loadArea() {
        val url = URLProviderUtil.getHouseConfigURL()
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("区县信息加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->
                    val config = GsonUtil.getInstance().fromJson(body.string(), Detail::class.java)

                    //设置默认区县信息
                    area = config[0].key
                    ThreadUtil.runOnUiThread { loadData() }

                    ThreadUtil.runOnUiThread {
                        try {
                            config.forEach {
                                val item = tab.newTab().setText(it.value)
                                item.tag = it.key
                                tab.addTab(item)
                            }

                            tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                                override fun onTabReselected(p: TabLayout.Tab) { }

                                override fun onTabUnselected(p: TabLayout.Tab) { }

                                override fun onTabSelected(p: TabLayout.Tab) {
                                    area = p.tag.toString()
                                    loadData()
                                }
                            })
                        } catch (e: Exception) {
                            SecondHouseLogger.e("捕获切换过快的异常")
                        }
                    }
                }
            }
        })
    }

    private fun loadData() {
        loadMore()
    }

    private fun loadMore(offset: Int = 0) {
        val area = area ?: return

        val url = URLProviderUtil.getHouseMessageURL(area, offset)
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("房源信息加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->
                    val house = GsonUtil.getInstance().fromJson(body.string(), House::class.java)

                    house.forEach {
                        if (it.picture.isEmpty()) {
                            it.picture = "https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouse/house_priture_default.jpg"
                        }
                    }

                    try {
                        ThreadUtil.runOnUiThread {
                            if (offset == 0) {
                                adapter.update(house)
                                refresh?.finishRefresh()
                            } else {
                                if (house.isNotEmpty()) {
                                    adapter.loadMore(house)
                                }
                                refresh?.finishLoadMore()
                            }
                        }
                    } catch (e: Exception) {
                        SecondHouseLogger.e("refresh is null")
                    }
                }
            }
        })
    }
}
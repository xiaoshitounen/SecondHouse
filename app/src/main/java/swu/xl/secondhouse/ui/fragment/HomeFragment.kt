package swu.xl.secondhouse.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import swu.xl.secondhouse.R
import swu.xl.secondhouse.adapter.NewsAdapter
import swu.xl.secondhouse.base.BaseFragment
import swu.xl.secondhouse.log.SecondHouseLogger
import swu.xl.secondhouse.manager.CityManager
import swu.xl.secondhouse.manager.OnCityChangeListener
import swu.xl.secondhouse.model.News
import swu.xl.secondhouse.util.*
import java.io.IOException
import java.lang.Exception

class HomeFragment : BaseFragment() {
    companion object {
        private const val DEFAULT_NUMBER = 10
    }
    private var limit = DEFAULT_NUMBER
    private val adapter by lazy { NewsAdapter() }

    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_home, null)
    }

    override fun initData() {
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
        refresh.autoRefresh()
        refresh.setOnRefreshListener { loadNews(10) }
        refresh.setOnLoadMoreListener {
            limit += DEFAULT_NUMBER
            loadNews(limit)
        }
    }

    override fun initListener() {
        CityManager.registerOnCityChangeListener(object : OnCityChangeListener {
            override fun onCityChange(city: String) {
                loadNews(DEFAULT_NUMBER)
            }
        })
    }

    private fun loadNews(number: Int) {
        val url = URLProviderUtil.getNewsURL(number)
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("最近三天的新闻数据加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    val news = GsonUtil.getInstance().fromJson(it.string(), News::class.java)

                    ThreadUtil.runOnUiThread {
                        adapter.setData(news.data)
                        try {
                            refresh?.finishRefresh()
                            refresh?.finishLoadMore()
                        } catch (e: Exception) {
                            SecondHouseLogger.e("refresh is null")
                        }
                    }
                }
            }
        })
    }
}
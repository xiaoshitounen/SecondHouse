package swu.xl.secondhouse.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.Request
import swu.xl.secondhouse.R
import swu.xl.secondhouse.adapter.HouseAdapter
import swu.xl.secondhouse.base.BaseActivity
import swu.xl.secondhouse.manager.CityManager
import swu.xl.secondhouse.manager.UserManager
import swu.xl.secondhouse.model.HouseItem
import swu.xl.secondhouse.sql.database.SecondHouseRoomBase
import swu.xl.secondhouse.util.*

class HouseListActivity : BaseActivity() {
    companion object {
        const val TITLE_LOVE = "我的喜欢"
        const val TITLE_RECORD = "浏览记录"
        private const val TITLE = "title"

        fun start(context: Context, title: String) {
            val intent = Intent(context, HouseListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(TITLE, title)
            context.startActivity(intent)
        }
    }

    private val adapter by lazy { HouseAdapter() }

    override fun getLayoutId() = R.layout.activity_house

    override fun initData() {
        val title = intent.getStringExtra(TITLE)
        titlebar.leftTextView.text = title

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        refresh.autoRefresh()
        refresh.setOnRefreshListener { loadMore() }
        refresh.setOnLoadMoreListener { loadMore(adapter.itemCount) }
    }

    private fun loadMore(offset: Int = 0) {
        val type = intent.getStringExtra(TITLE)
        val temp = mutableListOf<HouseItem>()
        val user = UserManager.getCurrentUser() ?: return

        AppExecutors.Default.execute {
            if (TextUtils.equals(type, TITLE_RECORD)) {
                SecondHouseRoomBase.getRoomBase(this).recordDao().queryRecord(offset, user.id).forEach {
                    if (TextUtils.equals(it.city, CityManager.city)) {
                        val url = URLProviderUtil.getHouseSimpleURL(it.h_id)
                        val request = Request.Builder().url(url).get().build()
                        val response = HttpClient.getInstance().newCall(request).execute().body()?.string() ?: return@execute
                        val house = GsonUtil.getInstance().fromJson(response, HouseItem::class.java)
                        temp.add(house)
                    }
                }
            } else {
                //我的喜欢
                SecondHouseRoomBase.getRoomBase(this).loveDao().queryLove(offset, user.id).forEach {
                    if (TextUtils.equals(it.city, CityManager.city)) {
                        val url = URLProviderUtil.getHouseSimpleURL(it.h_id)
                        val request = Request.Builder().url(url).get().build()
                        val response = HttpClient.getInstance().newCall(request).execute().body()?.string() ?: return@execute
                        val house = GsonUtil.getInstance().fromJson(response, HouseItem::class.java)
                        temp.add(house)
                    }
                }
            }

            ThreadUtil.runOnUiThread {
                //更新数据
                if (offset == 0) {
                    adapter.update(temp)
                    refresh?.finishRefresh()
                } else {
                    adapter.loadMore(temp)
                    refresh?.finishLoadMore()
                }
            }
        }
    }
}
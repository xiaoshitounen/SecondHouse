package swu.xl.secondhouse.ui.activity

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import cc.shinichi.library.ImagePreview
import com.google.android.material.tabs.TabLayout
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import swu.xl.secondhouse.R
import swu.xl.secondhouse.adapter.HouseDetailAdapter
import swu.xl.secondhouse.adapter.ImagePageAdapter
import swu.xl.secondhouse.base.BaseActivity
import swu.xl.secondhouse.manager.CityManager
import swu.xl.secondhouse.manager.UserManager
import swu.xl.secondhouse.model.Detail
import swu.xl.secondhouse.model.HouseItem
import swu.xl.secondhouse.model.Picture
import swu.xl.secondhouse.sql.database.SecondHouseRoomBase
import swu.xl.secondhouse.sql.entity.LoveEntity
import swu.xl.secondhouse.sql.entity.RecordEntity
import swu.xl.secondhouse.thirdparty.banner.DataBean
import swu.xl.secondhouse.thirdparty.banner.ImageNetAdapter
import swu.xl.secondhouse.ui.fragment.PictureFragment
import swu.xl.secondhouse.util.*
import java.io.IOException

class DetailActivity : BaseActivity() {
    companion object {
        private const val HOUSE = "SecondHouseID"
        private const val DEFAULT_DES = "默认"
        private const val DEFAULT_PIC = "https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouse/house_priture_default.jpg"

        fun loadDetail(context: Context, house: Int) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(HOUSE, house)
            context.startActivity(intent)
        }
    }

    private val baseAdapter by lazy { HouseDetailAdapter() }
    private val businessAdapter by lazy { HouseDetailAdapter() }
    private var isLove = false

    override fun getLayoutId() = R.layout.activity_detail

    override fun initData() {
        base_list.layoutManager = GridLayoutManager(this, 2)
        base_list.adapter = baseAdapter
        business_list.layoutManager = GridLayoutManager(this, 2)
        business_list.adapter = businessAdapter

        val house = intent.getIntExtra(HOUSE, 0)
        loadHouse(house)
        loadBase(house)
        loadBusiness(house)
        loadPicture(house)
        loadSpecial(house)

        record(house)
        love(house)
        changeLove(house)
    }

    private fun record(house: Int) {
        if (UserManager.isUserLogin()) {
            val user = UserManager.getCurrentUser() ?: return
            AppExecutors.Default.execute {
                val dao = SecondHouseRoomBase.getRoomBase(this@DetailActivity).recordDao()

                //先判断有没有浏览过
                dao.queryAllRecord(user.id).forEach { record ->
                    if (house == record.h_id) {
                        dao.deleteRecord(record)
                        return@forEach
                    }
                }

                //继续插入
                dao.insertRecord(RecordEntity(0, user.id, house, CityManager.city))
            }
        }
    }

    private fun love(house: Int) {
        if (UserManager.isUserLogin()) {
            val user = UserManager.getCurrentUser() ?: return
            AppExecutors.Default.execute {
                val list = SecondHouseRoomBase.getRoomBase(this).loveDao().querySimpleLove(user.id, house)
                if (list.isNotEmpty()) {
                    ThreadUtil.runOnUiThread {
                        isLove = true
                        love.setImageResource(R.drawable.house_love)
                    }
                }
            }
        }
    }

    private fun changeLove(house: Int) {
        love.setOnClickListener {
            if (isLove) {
                love.setImageResource(R.drawable.house_love_board)
            } else {
                love.setImageResource(R.drawable.house_love)
            }

            isLove = !isLove

            val user = UserManager.getCurrentUser() ?: return@setOnClickListener
            AppExecutors.Default.execute {
                val dao = SecondHouseRoomBase.getRoomBase(this).loveDao()

                if (isLove) {
                    //添加喜欢
                    dao.insertLove(LoveEntity(0, user.id, house, CityManager.city))
                } else {
                    //移除喜欢
                    val list = dao.querySimpleLove(user.id, house)
                    list.forEach { dao.deleteLove(it) }
                }
            }
        }
    }

    private fun loadHouse(house: Int) {
        val url = URLProviderUtil.getHouseSimpleURL(house)
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("房源数据加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let {
                    val house = GsonUtil.getInstance().fromJson(it.string(), HouseItem::class.java)

                    //解析出户型信息
                    val houseType = house.description.split('|')[0]
                    val houseSquare = house.description.split('|')[1]

                    ThreadUtil.runOnUiThread {
                        name.text = house.name
                        community.text = Html.fromHtml("<u>${house.community_name}</u>")
                        community.setOnClickListener { WebViewActivity.loadURL(this@DetailActivity, house.community_url) }
                        price.text = "${house.price}万元"
                        square.text = houseSquare
                        type.text = houseType
                    }
                }
            }
        })
    }

    private fun loadBase(house: Int) {
        val url = URLProviderUtil.getHouseBaseURL(house)
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("基本信息数据加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->
                    val detail = GsonUtil.getInstance().fromJson(body.string(), Detail::class.java)

                    ThreadUtil.runOnUiThread {
                        baseAdapter.update(detail)
                    }
                }
            }
        })
    }

    private fun loadBusiness(house: Int) {
        val url = URLProviderUtil.getHouseBusinessURL(house)
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("交易信息数据加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->
                    val detail = GsonUtil.getInstance().fromJson(body.string(), Detail::class.java)

                    ThreadUtil.runOnUiThread {
                        businessAdapter.update(detail)
                    }
                }
            }
        })
    }

    private fun loadPicture(house: Int) {
        val url = URLProviderUtil.getHousePictureURL(house)
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("图片信息数据加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->
                    val detail = GsonUtil.getInstance().fromJson(body.string(), Picture::class.java)

                    ThreadUtil.runOnUiThread {
                        val bean = mutableListOf<DataBean>()
                        detail.forEach {
                            bean.add(DataBean(it.picture, it.description, 1))
                        }

                        if (detail.isEmpty()) {
                            bean.add(DataBean(DEFAULT_PIC, DEFAULT_DES, 1))
                        }

                        banner.adapter = ImageNetAdapter(bean)
                        banner.indicator = CircleIndicator(this@DetailActivity)
                        banner.setBannerGalleryEffect(18, 10)
                    }

                    ThreadUtil.runOnUiThread {
                        val imageInfo = mutableListOf<String>()
                        detail.forEach {
                            imageInfo.add(it.picture)
                        }

                        val titles = mutableListOf<String>()
                        val fragments = mutableListOf<Fragment>()
                        detail.forEachIndexed { index, element ->
                            titles.add(element.description)
                            fragments.add(PictureFragment(element.picture,
                                View.OnClickListener {
                                    ImagePreview.getInstance()
                                        .setContext(this@DetailActivity)
                                        .setIndex(index)
                                        .setImageList(imageInfo)
                                        .setShowCloseButton(true)
                                        .setEnableDragClose(true)
                                        .setEnableUpDragClose(true)
                                        .start()
                                }))
                        }

                        if (detail.isEmpty()) {
                            titles.add(DEFAULT_DES)
                            fragments.add(PictureFragment(DEFAULT_PIC,
                                View.OnClickListener {
                                    ImagePreview.getInstance()
                                        .setContext(this@DetailActivity)
                                        .setImage(DEFAULT_PIC)
                                        .setShowCloseButton(true)
                                        .setEnableDragClose(true)
                                        .setEnableUpDragClose(true)
                                        .start()
                                }
                            ))
                        }

                        image_page.adapter = ImagePageAdapter(
                            supportFragmentManager,
                            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                            fragments,
                            titles
                        )

                        picture_tab.setupWithViewPager(image_page)
                    }
                }
            }
        })
    }

    private fun loadSpecial(house: Int) {
        val url = URLProviderUtil.getHouseSpecialURL(house)
        val request = Request.Builder().url(url).get().build()
        HttpClient.getInstance().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.toast("特色信息数据加载失败")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->
                    val detail = GsonUtil.getInstance().fromJson(body.string(), Detail::class.java)

                    ThreadUtil.runOnUiThread {
                        detail.forEach {
                            val item = special_tab.newTab().setText(it.key)
                            item.tag = it.value
                            special_tab.addTab(item)
                        }

                        if (detail.isEmpty()) {
                            special_card.visibility = View.GONE
                        } else {
                            special_text.text = detail[0].value
                        }

                        special_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                            override fun onTabReselected(p: TabLayout.Tab) { }

                            override fun onTabUnselected(p: TabLayout.Tab) { }

                            override fun onTabSelected(p: TabLayout.Tab) {
                                special_text.text = p.tag.toString()
                            }
                        })
                    }
                }
            }
        })
    }
}
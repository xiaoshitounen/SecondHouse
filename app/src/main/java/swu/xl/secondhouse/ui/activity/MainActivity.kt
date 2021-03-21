package swu.xl.secondhouse.ui.activity

import android.view.View
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.activity_main.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseActivity
import swu.xl.secondhouse.log.SecondHouseLogger
import swu.xl.secondhouse.manager.CityManager
import swu.xl.secondhouse.manager.OnCityChangeListener
import swu.xl.secondhouse.util.FragmentUtil

class MainActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_main

    override fun initData() {
        titlebar.leftTextView.text = CityManager.getCityValue()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, FragmentUtil.fragmentUtil.getFragment(0), "default")
            commit()
        }
    }

    override fun initListener() {
        titlebar.setListener { _, action, _ ->
            if (action == CommonTitleBar.ACTION_RIGHT_BUTTON) {
                SecondHouseLogger.i("点击了定位")
                CityManager.changeCity()
            }
        }

        bottombar.setOnNavigationItemSelectedListener {
            supportFragmentManager.beginTransaction().apply {
                if (it.itemId == R.id.tab_user) {
                    titlebar.visibility = View.GONE
                } else {
                    titlebar.visibility = View.VISIBLE
                }

                replace(R.id.container, FragmentUtil.fragmentUtil.getFragment(it.itemId), it.itemId.toString())
                commit()
            }
            true
        }

        CityManager.registerOnCityChangeListener(object : OnCityChangeListener {
            override fun onCityChange(city: String) {
                titlebar.leftTextView.text = CityManager.getCityValue()
            }
        })
    }
}
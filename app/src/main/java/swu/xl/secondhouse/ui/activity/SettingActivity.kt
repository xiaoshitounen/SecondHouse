package swu.xl.secondhouse.ui.activity

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_setting.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseActivity
import swu.xl.secondhouse.manager.UserManager
import swu.xl.secondhouse.sql.database.SecondHouseRoomBase
import swu.xl.secondhouse.util.AppExecutors
import swu.xl.secondhouse.util.ThreadUtil
import swu.xl.secondhouse.util.ToastUtil

class SettingActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_setting

    override fun initListener() {
        update_user.setOnClickListener {
            startActivity(Intent(this, UpdateActivity::class.java))
        }

        delete_love.setOnClickListener {
            AppExecutors.Default.execute {
                SecondHouseRoomBase.getRoomBase(this).loveDao().deleteAll()
                ToastUtil.toast("数据已经清空")
            }
        }

        delete_record.setOnClickListener {
            AppExecutors.Default.execute {
                SecondHouseRoomBase.getRoomBase(this).recordDao().deleteAll()
                ToastUtil.toast("数据已经清空")
            }
        }

        protocol.setOnClickListener {
            WebViewActivity.loadURL(this, "file:///android_asset/protocol.html")
        }

        logout.setOnClickListener {
            UserManager.logout()
            finish()
        }
    }
}
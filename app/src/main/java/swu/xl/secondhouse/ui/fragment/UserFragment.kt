package swu.xl.secondhouse.ui.fragment

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseFragment
import swu.xl.secondhouse.manager.OnUserChangeCallBack
import swu.xl.secondhouse.manager.UserManager
import swu.xl.secondhouse.sql.entity.UserEntity
import swu.xl.secondhouse.ui.activity.FeedBackActivity
import swu.xl.secondhouse.ui.activity.HouseListActivity
import swu.xl.secondhouse.ui.activity.LoginActivity
import swu.xl.secondhouse.ui.activity.SettingActivity
import swu.xl.secondhouse.util.ToastUtil

class UserFragment : BaseFragment() {
    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_user, null)
    }

    override fun initData() {
        if (UserManager.isUserLogin()) {
            activity?.let {
                UserManager.getCurrentUser()?.let { user ->
                    Glide.with(it).load(user.picture).into(icon)
                    nickname.text = user.nickname
                }
            }
        }
    }

    override fun initListener() {
        UserManager.registerOnUserChangeListener(object : OnUserChangeCallBack() {
            override fun onUserLogin(user: UserEntity) {
                activity?.let {
                    Glide.with(it).load(user.picture).into(icon)
                    nickname.text = user.nickname
                }
            }

            override fun onUserLogout() {
                icon.setImageResource(R.drawable.user_default_picture)
                nickname.text = "点击头像登录"
            }
        })

        icon.setOnClickListener {
            if (!UserManager.isUserLogin()) {
                //前往登录注册页面
                activity?.let { startActivity(Intent(it, LoginActivity::class.java)) }
            }
        }

        user_love.setOnClickListener {
            if (UserManager.isUserLogin()) {
                activity?.let { HouseListActivity.start(it, HouseListActivity.TITLE_LOVE)}
            } else {
                ToastUtil.toast("请先登录")
            }
        }

        user_record.setOnClickListener {
            if (UserManager.isUserLogin()) {
                activity?.let { HouseListActivity.start(it, HouseListActivity.TITLE_RECORD)}
            } else {
                ToastUtil.toast("请先登录")
            }
        }

        user_feedback.setOnClickListener {
            if (UserManager.isUserLogin()) {
                activity?.let { FeedBackActivity.start(it)}
            } else {
                ToastUtil.toast("请先登录")
            }
        }

        user_setting.setOnClickListener {
            if (UserManager.isUserLogin()) {
                activity?.let { SettingActivity.start(it)}
            } else {
                ToastUtil.toast("请先登录")
            }
        }
    }
}
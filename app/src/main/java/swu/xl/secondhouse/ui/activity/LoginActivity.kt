package swu.xl.secondhouse.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_login.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseActivity
import swu.xl.secondhouse.manager.UserManager
import swu.xl.secondhouse.sql.database.SecondHouseRoomBase
import swu.xl.secondhouse.util.AppExecutors
import swu.xl.secondhouse.util.PhoneUtil
import swu.xl.secondhouse.util.ThreadUtil
import swu.xl.secondhouse.util.ToastUtil

class LoginActivity : BaseActivity() {
    companion object {
        const val REQUEST_CODE = 10010
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun initListener() {
        login.setOnClickListener {
            if (account.text.toString().isEmpty()) {
                ToastUtil.toast("请输入手机号")
                return@setOnClickListener
            }

            if (!PhoneUtil.isPhoneNumberValid(account.text.toString())) {
                ToastUtil.toast("手机号不合法")
                return@setOnClickListener
            }

            if (password.text.toString().isEmpty()) {
                ToastUtil.toast("请输入密码")
                return@setOnClickListener
            }

            AppExecutors.Default.execute {
                SecondHouseRoomBase.getRoomBase(this).userDao().queryUser().forEach {
                    if (TextUtils.equals(it.account, account.text.toString())) {
                        if (TextUtils.equals(it.password, password.text.toString())) {
                            //输入正确
                            ThreadUtil.runOnUiThread {
                                UserManager.login(it)
                                finish()
                            }

                            return@execute
                        } else {
                            ToastUtil.toast("密码输入错误")
                        }

                        return@forEach
                    }
                }

                ToastUtil.toast("该手机号还没有注册")
            }
        }

        register.setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                account.setText(data.getStringExtra(RegisterActivity.ACCOUNT))
                password.setText(data.getStringExtra(RegisterActivity.PASSWORD))
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
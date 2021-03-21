package swu.xl.secondhouse.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import kotlinx.android.synthetic.main.activity_login.register
import kotlinx.android.synthetic.main.activity_register.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseActivity
import swu.xl.secondhouse.sql.database.SecondHouseRoomBase
import swu.xl.secondhouse.sql.entity.UserEntity
import swu.xl.secondhouse.tencent.TencentCOS
import swu.xl.secondhouse.thirdparty.easyphoto.GlideEngine
import swu.xl.secondhouse.util.AppExecutors
import swu.xl.secondhouse.util.PhoneUtil
import swu.xl.secondhouse.util.ThreadUtil
import swu.xl.secondhouse.util.ToastUtil
import java.io.File
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegisterActivity : BaseActivity() {
    companion object {
        const val ACCOUNT = "account"
        const val PASSWORD = "password"
    }

    private var bitmap: String = ""

    override fun getLayoutId() = R.layout.activity_register

    override fun initListener() {
        icon.setOnClickListener {
            EasyPhotos.createAlbum(this, true,false, GlideEngine.getInstance())
                .setFileProviderAuthority("androidx.core.content.FileProvider")
                .setCount(1)
                .start(object : SelectCallback() {
                    override fun onResult(photos: ArrayList<Photo>, isOriginal: Boolean) {
                        bitmap = photos[0].path
                        icon.setImageURI(Uri.fromFile(File(bitmap)))
                    }

                    override fun onCancel() {
                        ToastUtil.toast("未选择照片")
                    }
                })
        }

        register.setOnClickListener {
            if (bitmap.isEmpty()) {
                ToastUtil.toast("请选择照片")
                return@setOnClickListener
            }

            if (nickname.text.toString().isEmpty()) {
                ToastUtil.toast("请输入昵称")
                return@setOnClickListener
            }

            if (account_up.text.toString().isEmpty()) {
                ToastUtil.toast("请输入手机号")
                return@setOnClickListener
            }

            if (!PhoneUtil.isPhoneNumberValid(account_up.text.toString())) {
                ToastUtil.toast("手机号不合法")
                return@setOnClickListener
            }

            if (password_up.text.toString().isEmpty()) {
                ToastUtil.toast("请输入密码")
                return@setOnClickListener
            }

            AppExecutors.Default.execute {
                val dao = SecondHouseRoomBase.getRoomBase(this).userDao()
                dao.queryUser().forEach {
                    if (TextUtils.equals(it.account, account_up.text.toString())) {
                        ToastUtil.toast("手机号已经注册过了")
                        return@execute
                    }
                }

                TencentCOS.upload(this, bitmap) {
                    SecondHouseRoomBase.getRoomBase(this).userDao().insertUser(
                        UserEntity(
                            0,
                            nickname.text.toString(),
                            it,
                            account_up.text.toString(),
                            password_up.text.toString()
                        )
                    )

                    ThreadUtil.runOnUiThread {
                        val intent = Intent().apply {
                            putExtra(ACCOUNT, account_up.text.toString())
                            putExtra(PASSWORD, password_up.text.toString())
                        }
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}
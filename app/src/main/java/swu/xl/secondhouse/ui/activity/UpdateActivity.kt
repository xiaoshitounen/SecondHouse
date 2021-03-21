package swu.xl.secondhouse.ui.activity

import android.net.Uri
import android.text.TextUtils
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import kotlinx.android.synthetic.main.activity_register.icon
import kotlinx.android.synthetic.main.activity_update.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseActivity
import swu.xl.secondhouse.manager.UserManager
import swu.xl.secondhouse.sql.database.SecondHouseRoomBase
import swu.xl.secondhouse.thirdparty.easyphoto.GlideEngine
import swu.xl.secondhouse.util.AppExecutors
import swu.xl.secondhouse.util.ThreadUtil
import swu.xl.secondhouse.util.ToastUtil
import java.io.File
import java.util.*

class UpdateActivity : BaseActivity() {
    private var bitmap: String = ""

    override fun getLayoutId() = R.layout.activity_update

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

        update.setOnClickListener {
            if (bitmap.isEmpty()) {
                ToastUtil.toast("请选择照片")
                return@setOnClickListener
            }

            if (nickname_up.text.toString().isEmpty()) {
                ToastUtil.toast("请输入昵称")
                return@setOnClickListener
            }

            if (password.text.toString().isEmpty()) {
                ToastUtil.toast("请输入密码")
                return@setOnClickListener
            }

            if (password_again.text.toString().isEmpty()) {
                ToastUtil.toast("请输再次入密码")
                return@setOnClickListener
            }

            if (!TextUtils.equals(password.text.toString(), password_again.text.toString())) {
                ToastUtil.toast("两次密码输入不一致")
                password.setText("")
                password_again.setText("")
                return@setOnClickListener
            }

            AppExecutors.Default.execute {
                val user = UserManager.getCurrentUser() ?: return@execute
                user.nickname = nickname_up.text.toString()
                user.picture = bitmap
                user.password = password.text.toString()

                SecondHouseRoomBase.getRoomBase(this).userDao().updateUser(user)
                ThreadUtil.runOnUiThread { UserManager.login(user) }
                ToastUtil.toast("用户信息修改成功")
                finish()
            }
        }
    }
}
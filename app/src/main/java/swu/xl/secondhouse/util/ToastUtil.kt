package swu.xl.secondhouse.util

import android.widget.Toast
import swu.xl.secondhouse.application.MyApplication

object ToastUtil {
    fun toast(msg: String) {
        ThreadUtil.runOnUiThread {
            Toast.makeText(MyApplication.instance, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
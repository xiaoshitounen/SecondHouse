package swu.xl.secondhouse.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object SimpleData {
    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd")

    fun format(time: Long): String {
        return format.format(time)
    }
}


package swu.xl.secondhouse.util

import com.google.gson.Gson

object GsonUtil {
    private val gson = Gson()

    fun getInstance() = gson
}
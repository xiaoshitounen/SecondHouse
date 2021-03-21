package swu.xl.secondhouse.manager

import android.text.TextUtils
import java.util.concurrent.CopyOnWriteArrayList

object CityManager {
    private const val CQ = "cq"
    private const val GZ = "gz"
    private val listeners = CopyOnWriteArrayList<OnCityChangeListener>()

    var city = CQ

    fun getCityValue(): String {
        return when (city) {
            CQ -> "重庆"
            GZ -> "广州"
            else -> "重庆"
        }
    }

    fun changeCity() {
        //改变城市信息
        city = if (TextUtils.equals(city, GZ)) CQ else GZ

        //修改相关数据
        listeners.forEach { it.onCityChange(city) }
    }

    fun registerOnCityChangeListener(listener: OnCityChangeListener) {
        if (listener !in listeners) {
            listeners.add(listener)
        }
    }
}

interface OnCityChangeListener {
    fun onCityChange(city: String)
}
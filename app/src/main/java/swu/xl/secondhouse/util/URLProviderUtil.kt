package swu.xl.secondhouse.util

import swu.xl.secondhouse.manager.CityManager
import java.text.SimpleDateFormat
import java.util.*

object URLProviderUtil {
    private const val HOUSE_API = "https://api.ershoufangdata.com/api/"
    private const val HOUSE_PREFIX = "http://182.254.228.71/secondhouse/"

    fun getNewsURL(limit: Int = 10): String {
        val defaultCount = 1000
        //时间格式化后：2001-09-09 09:46:40，以此作为开始时间
        val defaultTime = 1000000000
        return HOUSE_API + "news?count=$defaultCount&limit=$limit&stime=$defaultTime&city=${CityManager.city}"
    }

    fun getHouseUpDownURL(): String {
        return HOUSE_API + "stat/guapan?city=${CityManager.city}&timerange=month"
    }

    fun getHouseSealURL(): String {
        return HOUSE_API + "stat/guapan?city=${CityManager.city}&date=2021-02-01"
    }

    fun getHouseIndexURL(): String {
        return HOUSE_API + "volume/tongjiju?city=${CityManager.city}"
    }

    fun getHouseConfigURL(): String {
        return HOUSE_PREFIX + "config.php?city=${CityManager.city}"
    }

    fun getHouseMessageURL(area: String, offset: Int): String {
        return HOUSE_PREFIX + "message.php?city=${CityManager.city}&area=$area&offset=$offset"
    }

    fun getHouseSimpleURL(house: Int): String {
        return HOUSE_PREFIX + "house.php?city=${CityManager.city}&house=$house"
    }

    fun getHouseBaseURL(house: Int): String {
        return HOUSE_PREFIX + "base.php?city=${CityManager.city}&house=$house"
    }

    fun getHouseBusinessURL(house: Int): String {
        return HOUSE_PREFIX + "business.php?city=${CityManager.city}&house=$house"
    }

    fun getHouseSpecialURL(house: Int): String {
        return HOUSE_PREFIX + "special.php?city=${CityManager.city}&house=$house"
    }

    fun getHousePictureURL(house: Int): String {
        return HOUSE_PREFIX + "picture.php?city=${CityManager.city}&house=$house"
    }
}

fun main() {
    println(Test.timeFormat("1000000000"))
    println(Test.timeFormat("1613023021"))
    println(Test.timeFormat("1614606809"))
    println(Test.timeFormat((System.currentTimeMillis() / 1000).toString()))
    val time = System.currentTimeMillis() - (24 * 60 * 60 * 1000 * 20)
    println(Test.timeFormat((time / 1000).toString()))
}

object Test {
    fun timeFormat(second: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(Date((second+"000").toLong()))
    }
}

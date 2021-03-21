package swu.xl.secondhouse.log

import android.util.Log

object SecondHouseLogger {
    private const val TAG = "SecondHouseLogger"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun w(msg: String) {
        Log.w(TAG, msg)
    }

    fun e(msg: String) {
        Log.e(TAG, msg)
    }
}
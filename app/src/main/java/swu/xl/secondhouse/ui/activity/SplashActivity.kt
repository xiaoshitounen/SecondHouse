package swu.xl.secondhouse.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_splash.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseActivity

class SplashActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_splash

    override fun initData() {
        ImmersionBar.with(this).init()

        splash.animate().scaleX(1.0f).scaleY(1.0f).apply {
            duration = 2000
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    startActivityAndFinish<MainActivity>()
                }
            })
        }
    }
}
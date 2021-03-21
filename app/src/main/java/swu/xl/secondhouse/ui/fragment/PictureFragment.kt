package swu.xl.secondhouse.ui.fragment

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_picture.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseFragment

class PictureFragment(private val url: String, private val click: View.OnClickListener) : BaseFragment() {
    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_picture, null)
    }

    override fun initData() {
        Glide.with(this).load(url).into(image)
        image.setOnClickListener(click)
    }
}
package swu.xl.secondhouse.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_news.view.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.model.Data
import swu.xl.secondhouse.ui.activity.WebViewActivity
import swu.xl.secondhouse.util.SimpleData

class NewsItemView : RelativeLayout {
    constructor(context: Context?): super(context)
    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    init {
        View.inflate(context, R.layout.item_news, this)
    }

    fun setData(data: Data) {
        title.text = data.title
        //Glide加载http请求的图片很容易出错
        Glide.with(this).load(data.image_url.replace("http://", "https://")).into(img)
        source.text = data.source
        time.text = SimpleData.format(data.create_time.toLong() * 1000)
        item.setOnClickListener { WebViewActivity.loadURL(context, data.article_url) }
    }
}
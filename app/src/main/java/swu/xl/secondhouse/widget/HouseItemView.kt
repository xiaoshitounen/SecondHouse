package swu.xl.secondhouse.widget

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_house.view.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.model.HouseItem
import swu.xl.secondhouse.ui.activity.DetailActivity
import swu.xl.secondhouse.ui.activity.WebViewActivity
import java.lang.Exception

class HouseItemView : LinearLayout {
    constructor(context: Context?): super(context)
    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    init {
        View.inflate(context, R.layout.item_house, this)
    }

    fun setData(data: HouseItem) {
        Glide.with(this).load(data.picture.replace("http://", "https://")).into(picture)
        name.text = data.name
        description.text = data.description
        location.text = Html.fromHtml("<u>${data.location}</u>")
        price.text = "${data.price}万"
        unit_price.text = "${data.unit_price}元/平"

        location.setOnClickListener { WebViewActivity.loadURL(context, data.community_url) }
        item.setOnClickListener { DetailActivity.loadDetail(context, data.id.toInt()) }
    }
}
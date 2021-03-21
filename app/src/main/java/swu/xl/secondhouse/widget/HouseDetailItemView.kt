package swu.xl.secondhouse.widget

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_house_field.view.*
import swu.xl.secondhouse.R
import swu.xl.secondhouse.model.DetailItem

class HouseDetailItemView : LinearLayout {
    constructor(context: Context?): super(context)
    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    init {
        View.inflate(context, R.layout.item_house_field, this)
    }

    fun setData(data: DetailItem) {
        val text = SpannableString(data.key + "：" + data.value)
        text.setSpan(ForegroundColorSpan(Color.GRAY), 0, data.key.length + 1, 0)
        text.setSpan(ForegroundColorSpan(Color.BLACK), data.key.length + 1, text.length, 0)
        msg.text = text
    }
}
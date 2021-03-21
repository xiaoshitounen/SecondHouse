package swu.xl.secondhouse.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import swu.xl.secondhouse.model.DetailItem
import swu.xl.secondhouse.widget.HouseDetailItemView

class HouseDetailAdapter : RecyclerView.Adapter<HouseDetailAdapter.HouseDetailHolder>() {
    private val list = mutableListOf<DetailItem>()

    fun update(list: List<DetailItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseDetailHolder {
        return HouseDetailHolder(HouseDetailItemView(parent.context))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: HouseDetailHolder, position: Int) {
        val data = list[position]
        val itemView = holder.itemView as HouseDetailItemView
        itemView.setData(data)
    }

    class HouseDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
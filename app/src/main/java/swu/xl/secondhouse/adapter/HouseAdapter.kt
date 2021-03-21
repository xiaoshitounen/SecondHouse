package swu.xl.secondhouse.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import swu.xl.secondhouse.model.HouseItem
import swu.xl.secondhouse.widget.HouseItemView

class HouseAdapter : RecyclerView.Adapter<HouseAdapter.HouseHolder>() {
    private val list = mutableListOf<HouseItem>()

    fun update(list: List<HouseItem>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun loadMore(list: List<HouseItem>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseHolder {
        return HouseHolder(HouseItemView(parent.context))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: HouseHolder, position: Int) {
        val data = list[position]
        val itemView = holder.itemView as HouseItemView
        itemView.setData(data)
    }

    class HouseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
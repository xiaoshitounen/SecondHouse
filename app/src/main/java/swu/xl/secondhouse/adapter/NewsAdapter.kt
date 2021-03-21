package swu.xl.secondhouse.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import swu.xl.secondhouse.model.Data
import swu.xl.secondhouse.widget.NewsItemView

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {
    private val list = mutableListOf<Data>()

    fun setData(list: List<Data>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsHolder(NewsItemView(parent.context))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val data = list[position]
        val view = holder.itemView as NewsItemView
        view.setData(data)
    }

    class NewsHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
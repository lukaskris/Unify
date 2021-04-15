package id.co.app.core.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter <T: BaseViewHolder<N>, N: Any> : RecyclerView.Adapter<T>(){
    protected val data = mutableListOf<N>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.bind(data[position])
    }

    fun submitList(data: List<N>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}
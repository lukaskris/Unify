package id.co.app.core.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.app.core.viewholder.LoadingViewHolder

abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder<Any>>(){
    protected val data = mutableListOf<Any>()
    private val loadingModel = BaseLoadingModel()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        if(data[position] == loadingModel) return LoadingViewHolder.LAYOUT
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        return if(viewType == LoadingViewHolder.LAYOUT) LoadingViewHolder(parent) as BaseViewHolder<Any>
        else createViewHolders(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
        holder.bind(data[position])
    }

    fun showLoading(){
        if(!data.contains(loadingModel)) {
            data.add(loadingModel)
            notifyItemInserted(data.size)
        }
    }

    fun hideLoading(){
        if(data.contains(loadingModel)){
            data.remove(loadingModel)
            notifyItemRemoved(data.size)
        }
    }

    fun submitList(data: List<Any>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun appendList(data: List<Any>){
        hideLoading()
        val lastIndex = this.data.size
        this.data.addAll(data)
        notifyItemRangeInserted(lastIndex + 1, data.size)
    }

    abstract fun createViewHolders(parent: ViewGroup, viewType: Int): BaseViewHolder<Any>
}
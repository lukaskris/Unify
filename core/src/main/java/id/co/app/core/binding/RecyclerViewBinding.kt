package id.co.app.core.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import id.co.app.core.base.BaseAdapter
import id.co.app.core.base.BaseViewModel
import id.co.app.core.base.RecyclerViewPagination
import id.co.app.core.extension.whatIfNotNullAs

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        view.adapter = adapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    @JvmStatic
    @BindingAdapter("submitList")
    fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
        view.adapter.whatIfNotNullAs<BaseAdapter> { adapter ->
            itemList?.let { adapter.submitList(it) }
        }
    }

    @JvmStatic
    @BindingAdapter("paginationList")
    fun paginationPokemonList(view: RecyclerView, viewModel: BaseViewModel) {
        RecyclerViewPagination(
            recyclerView = view,
            isLoading = {
                val loading = viewModel.isLoading.value ?: false
                if(loading) (view.adapter as BaseAdapter).showLoading()
                loading
            },
            loadMore = viewModel::fetchLoadMore,
            onLast = { false }
        ).run {
            threshold = 8
        }
    }
}
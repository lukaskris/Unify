package id.co.app.core.base

import androidx.lifecycle.LiveData

interface PaginationListener {
    val isLoading: LiveData<Boolean>
    fun fetchLoadMore(page: Int)
}
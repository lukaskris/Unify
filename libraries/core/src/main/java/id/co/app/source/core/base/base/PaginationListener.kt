package id.co.app.source.core.base.base

import androidx.lifecycle.LiveData

interface PaginationListener {
    val isLoading: LiveData<Boolean>
    fun fetchPage(page: Int)
}
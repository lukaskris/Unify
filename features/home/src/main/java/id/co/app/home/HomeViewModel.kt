/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.app.core.base.BaseViewModel
import id.co.app.core.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val mainRepository: MainRepository
): BaseViewModel() {
    val fetchingIndex = MutableStateFlow(0)

    private val _isLoading = MutableLiveData(false)
    override val isLoading: LiveData<Boolean>
        get() = _isLoading

    val pokemonListFlow = fetchingIndex.flatMapLatest { page ->
        mainRepository.fetchPokemonList(
            page = page
        )
            .onStart {
                _isLoading.value = true
            }
            .onCompletion {
                _isLoading.value = false
            }
    }

    override fun fetchPage(page: Int) {
        if(_isLoading.value == false){
            fetchingIndex.value = page
        }
    }

    fun refreshPage(){
        fetchingIndex.value = -1
        fetchingIndex.value = 0
    }
}
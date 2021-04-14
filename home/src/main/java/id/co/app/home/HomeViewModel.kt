/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.app.core.base.Result
import id.co.app.core.domain.entities.Pokemon
import id.co.app.core.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val mainRepository: MainRepository
): ViewModel() {
    private val _homeLiveData = MutableLiveData<Result<List<Pokemon>>>()
    val homeLiveData: LiveData<Result<List<Pokemon>>> get() = _homeLiveData

    private val fetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    fun getInitialDataList(){
        viewModelScope.launch {
            mainRepository.fetchPokemonList(INITIAL_PAGE).collect {
                _homeLiveData.postValue(it)
            }
        }
    }

    companion object{
        private const val INITIAL_PAGE = 0
    }
}
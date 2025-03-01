package com.example.fetch_demo.view_model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch_demo.data.BaseRepository
import com.example.fetch_demo.data.FetchDataRepository
import com.example.fetch_demo.data.FetchDemoDataItem
import kotlinx.coroutines.launch

class DataViewModel(repository: FetchDataRepository): BaseViewModel<FetchDataRepository>(repository) {

    private val _dataItems = MutableLiveData<Map<Int, List<FetchDemoDataItem>>>()
    val dataItem: LiveData<Map<Int, List<FetchDemoDataItem>>> = _dataItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchData() {
        _isLoading.value = true
        viewModelScope.launch {
            Log.i("xxx", "start to fetch data")
            repository.fetchData()
                .onSuccess { items ->
                    Log.i("xxx", "fetchData, onSuccess")
                    _dataItems.value = items
                    _isLoading.value = false
                }
                .onFailure {
                    Log.i("xxx", "fetchData, onFailure")
                    _isLoading.value = false
                }
        }
    }

    fun getListIds(): List<Int> {
        return _dataItems.value?.keys?.toList()?.sorted() ?: emptyList()
    }

    fun getItemsForListId(listId: Int): List<FetchDemoDataItem> {
        return _dataItems.value?.get(listId) ?: emptyList()
    }
}
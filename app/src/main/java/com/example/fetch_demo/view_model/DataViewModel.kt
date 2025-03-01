package com.example.fetch_demo.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fetch_demo.data.FetchDataRepository
import com.example.fetch_demo.data.FetchDemoDataItem
import kotlinx.coroutines.launch

/**
 * ViewModel for managing and providing data to the UI layer.
 * Handles data fetching operations and maintains UI state.
 *
 * @property repository The FetchDataRepository instance used to fetch and process data.
 */
class DataViewModel(repository: FetchDataRepository): BaseViewModel<FetchDataRepository>(repository) {

    /**
     * Internal mutable LiveData that stores the fetched data grouped by listId.
     */
    private val _dataItems = MutableLiveData<Map<Int, List<FetchDemoDataItem>>>()

    /**
     * Public immutable LiveData that exposes the fetched data to observers.
     * The data is structured as a map where:
     * - Key: listId (Integer)
     * - Value: List of FetchDemoDataItems belonging to that listId
     */
    val dataItem: LiveData<Map<Int, List<FetchDemoDataItem>>> = _dataItems

    /**
     * Internal mutable LiveData that tracks the loading state of data operations.
     */
    private val _isLoading = MutableLiveData<Boolean>()

    /**
     * Fetches data from the repository and updates the LiveData values.
     * Uses coroutines via viewModelScope to perform the network operation asynchronously.
     * Updates loading state before and after the operation.
     */
    fun fetchData() {
        _isLoading.value = true
        viewModelScope.launch {
            Log.i("xxx", "start to fetch data")
            repository.fetchData()
                .onSuccess { items ->
                    Log.d("xxx", "fetchData, onSuccess")
                    _dataItems.value = items
                    _isLoading.value = false
                }
                .onFailure {
                    Log.d("xxx", "fetchData, onFailure")
                    _isLoading.value = false
                }
        }
    }
}
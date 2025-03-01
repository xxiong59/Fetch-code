package com.example.fetch_demo.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fetch_demo.data.FetchDataRepository

/**
 * Factory class for creating DataViewModel instances.
 * This factory enables dependency injection of the repository into the ViewModel.
 *
 * @property repository The FetchDataRepository instance to be provided to the ViewModel.
 */
class DataViewModelFactory(
    private val repository: FetchDataRepository
): ViewModelProvider.Factory {
    /**
     * Creates a new instance of the requested ViewModel.
     *
     * @param modelClass The class of the ViewModel to create an instance of.
     * @return A new instance of the requested ViewModel type.
     * @throws IllegalArgumentException if the ViewModel class is not supported by this factory.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DataViewModel::class.java) -> return DataViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
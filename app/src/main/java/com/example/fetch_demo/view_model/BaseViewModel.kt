package com.example.fetch_demo.view_model

import androidx.lifecycle.ViewModel
import com.example.fetch_demo.data.BaseRepository

/**
 * Abstract base ViewModel class that serves as a foundation for all ViewModels in the application.
 * This class follows the MVVM architecture pattern and integrates with the Repository pattern.
 *
 * @param T The specific repository type that extends BaseRepository.
 *          This allows different ViewModels to work with different repository implementations
 *          while maintaining a consistent architecture.
 * @property repository The repository instance that provides data access functionality.
 *                     Child classes can access this property to perform data operations.
 */
abstract class BaseViewModel<T : BaseRepository>(protected val repository: T) : ViewModel()
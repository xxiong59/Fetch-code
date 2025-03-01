package com.example.fetch_demo.view_model

import androidx.lifecycle.ViewModel
import com.example.fetch_demo.data.BaseRepository

abstract class BaseViewModel<T : BaseRepository>(protected val repository: T) : ViewModel()
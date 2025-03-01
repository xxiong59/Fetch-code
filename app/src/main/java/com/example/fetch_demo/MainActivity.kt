package com.example.fetch_demo

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetch_demo.data.FetchDataRepository
import com.example.fetch_demo.network.FetchApiService
import com.example.fetch_demo.ui.component.DataAdapter
import com.example.fetch_demo.view_model.DataViewModel
import com.example.fetch_demo.view_model.DataViewModelFactory

/**
 * Main activity for the application that displays the fetched data.
 * Implements MVVM architecture pattern with a ViewModel and Repository.
 */
class MainActivity : ComponentActivity() {

    /**
     * UI components
     */
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    /**
     * Architecture components
     */
    private lateinit var viewModelFactory: DataViewModelFactory
    private lateinit var fetchDataRepository: FetchDataRepository
    private lateinit var dataViewModel: DataViewModel
    private lateinit var dataAdapter: DataAdapter

    /**
     * Initializes the activity and sets up the UI and data components.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    /**
     * Initializes all components of the activity in the correct order.
     */
    private fun init() {
        initView()
        initRepository()
        initViewModel()
        initData()
    }

    /**
     * Initializes the UI components and sets up the RecyclerView with its adapter and decorations.
     */
    private fun initView() {
        // Find views
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)

        // Set up RecyclerView
        dataAdapter = DataAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = dataAdapter

        // Add divider decoration
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider_layout)?.let {
            divider.setDrawable(it)
        }
        recyclerView.addItemDecoration(divider)
    }

    /**
     * Initializes the repository with the API service.
     */
    private fun initRepository() {
        val fetchApiService = FetchApiService.create()
        fetchDataRepository = FetchDataRepository(fetchApiService)
    }

    /**
     * Initializes the ViewModel with the repository and sets up data observation.
     */
    private fun initViewModel() {
        viewModelFactory = DataViewModelFactory(fetchDataRepository)
        dataViewModel = ViewModelProvider(this, viewModelFactory)[DataViewModel::class.java]
        observeViewModel()
    }

    /**
     * Triggers the initial data fetch.
     */
    private fun initData() {
        dataViewModel.fetchData()
    }

    /**
     * Sets up observers for the ViewModel's LiveData objects to react to data changes.
     */
    private fun observeViewModel() {
        dataViewModel.dataItem.observe(this) { items ->
            if (!items.isEmpty()) {
                dataAdapter.setData(items)
                tvEmpty.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.VISIBLE
            }
        }
    }
}
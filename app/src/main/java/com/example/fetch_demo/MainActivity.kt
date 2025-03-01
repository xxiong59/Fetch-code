package com.example.fetch_demo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetch_demo.data.FetchDataRepository
import com.example.fetch_demo.network.FetchApiService
import com.example.fetch_demo.ui.component.DataAdapter
import com.example.fetch_demo.ui.theme.FetchdemoTheme
import com.example.fetch_demo.view_model.DataViewModel
import com.example.fetch_demo.view_model.DataViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var switchDataDisplayModeButton: Button
    private lateinit var tvEmpty: TextView

    private lateinit var viewModelFactory: DataViewModelFactory
    private lateinit var fetchDataRepository: FetchDataRepository
    private lateinit var dataViewModel: DataViewModel
    private lateinit var dataAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        initView()
        initRepository()
        initViewModel()
        initData()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        switchDataDisplayModeButton = findViewById(R.id.btnSwitchDataMode)
        tvEmpty = findViewById(R.id.tvEmpty)
        dataAdapter = DataAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = dataAdapter
    }

    private fun initRepository() {
        val fetchApiService = FetchApiService.create()
        fetchDataRepository = FetchDataRepository(fetchApiService)
    }

    private fun initViewModel() {
        viewModelFactory = DataViewModelFactory(fetchDataRepository)
        dataViewModel = ViewModelProvider(this, viewModelFactory)[DataViewModel::class.java]
        observeViewModel()
    }

    private fun initData() {
        dataViewModel.fetchData()
    }

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
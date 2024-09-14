package com.onurkucuk.seriesexplorer.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.adapters.SeriesFeedAdapter
import com.onurkucuk.seriesexplorer.database.SeriesDatabase
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.network.SeriesRetrofitInstance
import com.onurkucuk.seriesexplorer.repository.SeriesRepository
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModelProviderFactory
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : SeriesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val seriesRepository = SeriesRepository(SeriesDatabase.invoke(this))
        val viewModelProviderFactory = SeriesViewModelProviderFactory(application, seriesRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(SeriesViewModel::class.java)

    }
}
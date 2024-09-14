package com.onurkucuk.seriesexplorer.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onurkucuk.seriesexplorer.repository.SeriesRepository

class SeriesViewModelProviderFactory(
    val app: Application,
    val seriesRepository: SeriesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeriesViewModel(app, seriesRepository) as T
    }
}
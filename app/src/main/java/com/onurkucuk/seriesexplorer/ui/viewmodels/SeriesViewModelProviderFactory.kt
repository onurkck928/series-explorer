package com.onurkucuk.seriesexplorer.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onurkucuk.seriesexplorer.repository.SeriesRepository

class SeriesViewModelProviderFactory(
    val seriesRepository: SeriesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeriesViewModel(seriesRepository) as T
    }
}
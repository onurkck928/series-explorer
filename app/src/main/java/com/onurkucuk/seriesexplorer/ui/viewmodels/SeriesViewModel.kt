package com.onurkucuk.seriesexplorer.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.onurkucuk.seriesexplorer.repository.SeriesRepository

class SeriesViewModel(
    val seriesRepository: SeriesRepository
) : ViewModel() {
}
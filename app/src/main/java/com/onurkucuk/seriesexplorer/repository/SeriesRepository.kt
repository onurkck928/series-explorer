package com.onurkucuk.seriesexplorer.repository

import com.onurkucuk.seriesexplorer.database.SeriesDatabase
import com.onurkucuk.seriesexplorer.network.SeriesRetrofitInstance

class SeriesRepository(
    val seriesDatabase: SeriesDatabase
) {
    suspend fun getSeriesList(pageNumber: Int) = SeriesRetrofitInstance.seriesAPI.getSeriesList()
}
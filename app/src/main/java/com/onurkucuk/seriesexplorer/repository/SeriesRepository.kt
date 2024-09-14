package com.onurkucuk.seriesexplorer.repository

import com.onurkucuk.seriesexplorer.database.SeriesDatabase
import com.onurkucuk.seriesexplorer.network.SeriesRetrofitInstance
import retrofit2.Retrofit

class SeriesRepository(
    val seriesDatabase: SeriesDatabase
) {
    suspend fun getSeriesList(pageNumber: Int) = SeriesRetrofitInstance.seriesAPI.getSeriesList()

    suspend fun getSearchedSeries(searchQuery: String, pageNumber: Int) =
        SeriesRetrofitInstance.seriesAPI.searchForSeries(searchQuery, pageNumber)
}
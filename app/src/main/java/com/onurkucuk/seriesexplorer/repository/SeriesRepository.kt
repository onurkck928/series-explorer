package com.onurkucuk.seriesexplorer.repository

import com.onurkucuk.seriesexplorer.database.SeriesDatabase
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.network.SeriesRetrofitInstance
import retrofit2.Retrofit

class SeriesRepository(
    val seriesDatabase: SeriesDatabase
) {
    // Network
    suspend fun getSeriesList(pageNumber: Int) = SeriesRetrofitInstance.seriesAPI.getSeriesList()

    suspend fun getSearchedSeries(searchQuery: String, pageNumber: Int) =
        SeriesRetrofitInstance.seriesAPI.searchForSeries(searchQuery, pageNumber)

    // Database

    suspend fun saveSeries(vararg series: Series) = seriesDatabase.getSeriesDao().saveSeries(*series)

    suspend fun removeSeries(series: Series) = seriesDatabase.getSeriesDao().removeSeries(series)

    fun getSavedSeries() = seriesDatabase.getSeriesDao().getSavedSeries()
}
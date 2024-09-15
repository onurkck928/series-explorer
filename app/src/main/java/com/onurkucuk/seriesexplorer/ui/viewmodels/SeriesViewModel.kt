package com.onurkucuk.seriesexplorer.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onurkucuk.seriesexplorer.SeriesApplication
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.models.SeriesResponse
import com.onurkucuk.seriesexplorer.repository.SeriesRepository
import com.onurkucuk.seriesexplorer.util.Resources
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class SeriesViewModel(
    app: Application,
    val seriesRepository: SeriesRepository
) : AndroidViewModel(app) {

    // For getting all series
    val seriesList: MutableLiveData<Resources<SeriesResponse>> = MutableLiveData()
    var seriesPageNumber = 1
    var seriesPageItemCount = 0
    var seriesListResponse: SeriesResponse? = null

    // For getting the searched series
    val searchedSeries: MutableLiveData<Resources<SeriesResponse>> = MutableLiveData()
    var searchedSeriesPageNumber = 1
    var searchedSeriesPageItemCount = 0
    var searchedSeriesResponse: SeriesResponse? = null

    // The list that stores series
    companion object {
         val seriesList = mutableListOf<Series>()
         val favouriteSeriesIdSet = mutableSetOf<Int>()
    }
    init {
        getSeriesList()
    }

    fun getSeriesList() = viewModelScope.launch {
        safeGetSeriesListCall()
    }

    fun getSearchedSeries(searchQuery: String) = viewModelScope.launch {
        safeGetSearchedSeriesCall(searchQuery)
    }

    private fun handleSeriesResponse(response: Response<SeriesResponse>) : Resources<SeriesResponse>{
        if(response.isSuccessful) {
            response.body()?.let {
                seriesPageNumber++
                seriesPageItemCount = it.results.size
                if(seriesListResponse == null) {
                    seriesListResponse = it
                } else {
                    val oldSeries = seriesListResponse?.results
                    val newSeries = it.results
                    oldSeries?.addAll(newSeries)
                }
                return Resources.Success(seriesListResponse ?: it)
            }
        }
        return Resources.Error(message = response.message())
    }

    private fun handleSearchedSeriesResponse(response: Response<SeriesResponse>) : Resources<SeriesResponse>{
        if(response.isSuccessful) {
            response.body()?.let {
                searchedSeriesPageNumber++
                searchedSeriesPageItemCount = it.results.size
                if(searchedSeriesResponse == null) {
                    searchedSeriesResponse = it
                } else {
                    val oldSeries = searchedSeriesResponse?.results
                    val newSeries = it.results
                    oldSeries?.addAll(newSeries)
                }
                return Resources.Success(searchedSeriesResponse ?: it)
            }
        }
        return Resources.Error(message = response.message())
    }

    fun saveSeries(vararg series: Series) = viewModelScope.launch {
        seriesRepository.saveSeries(*series)
    }

    fun removeSeries(series: Series) = viewModelScope.launch {
        seriesRepository.removeSeries(series)
    }

    fun getSavedSeries() = seriesRepository.getSavedSeries()

    fun isInFavouriteSeries(series: Series) : Boolean {
        return when {
            favouriteSeriesIdSet.contains(series.id) -> true
            else -> false
        }
    }

    private suspend fun safeGetSeriesListCall() {
        seriesList.postValue(Resources.Loading())
        try {
            if(hasInternetConnection()) {
                val response = seriesRepository.getSeriesList(seriesPageNumber)
                seriesList.postValue(handleSeriesResponse(response))
            } else {
                seriesList.postValue(Resources.Error(message = "NO INTERNET CONNECTION"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> seriesList.postValue(Resources.Error(message = "NETWORK FAILURE"))
                else -> seriesList.postValue(Resources.Error(message = "SOMETHING WENT WRONG"))
            }
        }
    }

    private suspend fun safeGetSearchedSeriesCall(searchQuery: String) {
        searchedSeries.postValue(Resources.Loading())
        try {
            if(hasInternetConnection()) {
                val response = seriesRepository.getSearchedSeries(searchQuery ,searchedSeriesPageNumber)
                searchedSeries.postValue(handleSearchedSeriesResponse(response))
            } else {
                searchedSeries.postValue(Resources.Error(message = "NO INTERNET CONNECTION"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchedSeries.postValue(Resources.Error(message = "NETWORK FAILURE"))
                else -> searchedSeries.postValue(Resources.Error(message = "SOMETHING WENT WRONG"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<SeriesApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
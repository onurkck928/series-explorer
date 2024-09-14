package com.onurkucuk.seriesexplorer.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.models.SeriesResponse
import com.onurkucuk.seriesexplorer.repository.SeriesRepository
import com.onurkucuk.seriesexplorer.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class SeriesViewModel(
    val seriesRepository: SeriesRepository
) : ViewModel() {

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


    init {
        getSeriesList()
    }

    fun getSeriesList() = viewModelScope.launch {
        seriesList.postValue(Resources.Loading())
        val response = seriesRepository.getSeriesList(seriesPageNumber)
        seriesList.postValue(handleSeriesResponse(response))

    }

    fun getSearchedSeries(searchQuery: String) = viewModelScope.launch {
        searchedSeries.postValue(Resources.Loading())
        val response = seriesRepository.getSearchedSeries(searchQuery, searchedSeriesPageNumber)
        seriesList.postValue(handleSearchedSeriesResponse(response))
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
}
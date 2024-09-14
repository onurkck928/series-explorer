package com.onurkucuk.seriesexplorer.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // For getting the searched series
    val searchedSeries: MutableLiveData<Resources<SeriesResponse>> = MutableLiveData()
    var searchedSeriesPageNumber = 1

    init {
        getSeriesList()
    }

    fun getSeriesList(pageNumber: Int = 1) = viewModelScope.launch {
        seriesList.postValue(Resources.Loading())
        val response = seriesRepository.getSeriesList(pageNumber)
        seriesList.postValue(handleSeriesResponse(response))

    }

    fun getSearchedSeries(searchQuery: String,) = viewModelScope.launch {
        searchedSeries.postValue(Resources.Loading())
        val response = seriesRepository.getSearchedSeries(searchQuery, searchedSeriesPageNumber)
        seriesList.postValue(handleSearchedSeriesResponse(response))
    }



    private fun handleSeriesResponse(response: Response<SeriesResponse>) : Resources<SeriesResponse>{
        if(response.isSuccessful) {
            response.body()?.let {
                return Resources.Success(it)
            }
        }
        return Resources.Error(message = response.message())
    }

    private fun handleSearchedSeriesResponse(response: Response<SeriesResponse>) : Resources<SeriesResponse>{
        if(response.isSuccessful) {
            response.body()?.let {
                return Resources.Success(it)
            }
        }
        return Resources.Error(message = response.message())
    }

}
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

    val seriesList: MutableLiveData<Resources<SeriesResponse>> = MutableLiveData()
    var seriesListPageNumber = 1

    fun getSeriesList(pageNumber: Int) = viewModelScope.launch {
        seriesList.postValue(Resources.Loading())
        val response = seriesRepository.getSeriesList(pageNumber)
        seriesList.postValue(handleSeriesResponse(response))

    }

    private fun handleSeriesResponse(response: Response<SeriesResponse>) : Resources<SeriesResponse>{
        if(response.isSuccessful) {
            response.body()?.let {
                return Resources.Success(it)
            }
        }
        return Resources.Error(message = response.message())
    }
}
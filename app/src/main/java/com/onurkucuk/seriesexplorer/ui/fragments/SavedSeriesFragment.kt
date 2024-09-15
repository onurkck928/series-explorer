package com.onurkucuk.seriesexplorer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.adapters.SeriesFeedAdapter
import com.onurkucuk.seriesexplorer.databinding.FragmentSavedSeriesBinding
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.ui.MainActivity
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel
import com.onurkucuk.seriesexplorer.util.Constants.Companion.SEARCH_SERIES_TIME_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SavedSeriesFragment : Fragment() {

    //View Binding
    private var _binding: FragmentSavedSeriesBinding? = null
    private val binding get() = _binding!!

    // Properties
    lateinit var viewModel: SeriesViewModel
    companion object {
        lateinit var savedSeriesFeedAdapter: SeriesFeedAdapter

    }
    lateinit var savedSeriesRecyclerView: RecyclerView
    lateinit var savedSeriesList: MutableList<Series>

    var isSavedSeriesInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSavedSeriesBinding.inflate(inflater, container, false)
        val view = binding.root

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        savedSeriesList = viewModel.getSavedSeries().value?.toMutableList() ?: mutableListOf()
        setupRecyclerView()

        savedSeriesFeedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("fragment", "SavedSeriesFragment")
                putSerializable("series", it)
            }
            findNavController().navigate(R.id.action_savedSeriesFragment_to_seriesDetailsFragment, bundle)
        }

        observeSavedSeriesList()


        //addSearchBar()

        //observeSearchedSeries()




    }

    private fun setupRecyclerView() {

        savedSeriesFeedAdapter = SeriesFeedAdapter(savedSeriesList, viewModel)
        savedSeriesRecyclerView = binding.savedSeriesRecycler

        savedSeriesRecyclerView.adapter = savedSeriesFeedAdapter
        savedSeriesRecyclerView.layoutManager = LinearLayoutManager(this.context)

        savedSeriesRecyclerView.addOnScrollListener(this.scrollListener)
    }

    private fun addSearchBar() {

        var job: Job? = null
        binding.searchBar.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_SERIES_TIME_DELAY)
                if(editable.toString().isNotEmpty()) {
                    viewModel.searchedSeriesPageNumber = 1
                    viewModel.searchedSeriesResponse = null
                    viewModel.getSearchedSeries(editable.toString())
                }
                else {
                    viewModel.seriesPageNumber = 1
                    viewModel.seriesListResponse = null
                    viewModel.getSeriesList()
                }
            }
        }
    }

    private fun observeSavedSeriesList() {

        viewModel.getSavedSeries().observe(requireActivity()) { newSavedSeriesList ->

            savedSeriesFeedAdapter.addSeriesList(newSavedSeriesList)
            if(!isSavedSeriesInitialized) {
                newSavedSeriesList.forEach {
                    SeriesViewModel.favouriteSeriesIdSet.add(it.id)
                }
                isSavedSeriesInitialized = true
            }
        }
    }

    private fun hideProgressBar() {
        isLoading = false
    }

    private fun showProgressBar() {
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = savedSeriesRecyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = if (binding.searchBar.text.isEmpty()) {
                totalItemCount >= viewModel.seriesPageItemCount
            } else {
                totalItemCount >= viewModel.searchedSeriesPageItemCount
            }

            val shouldPaginate = !isLoading && !isLastPage && isNotAtBeginning && isAtLastItem && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                if(binding.searchBar.text.isEmpty()) {
                    viewModel.getSeriesList()
                }
                else {
                    viewModel.getSearchedSeries(binding.searchBar.text.toString())
                }
                isScrolling = false

            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.onurkucuk.seriesexplorer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.adapters.SeriesFeedAdapter
import com.onurkucuk.seriesexplorer.databinding.FragmentSeriesFeedBinding
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.ui.MainActivity
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel
import com.onurkucuk.seriesexplorer.util.Constants.Companion.SEARCH_SERIES_TIME_DELAY
import com.onurkucuk.seriesexplorer.util.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SeriesFeedFragment : Fragment() {

    //View Binding
    private var _binding: FragmentSeriesFeedBinding? = null
    private val binding get() = _binding!!

    // Properties
    lateinit var viewModel: SeriesViewModel
    lateinit var seriesFeedAdapter: SeriesFeedAdapter
    lateinit var seriesList: MutableList<Series>
    lateinit var seriesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSeriesFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        seriesList = SeriesViewModel.seriesList

        setupRecyclerView()

        seriesFeedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("position", seriesList.indexOf(it))
            }
            findNavController().navigate(R.id.action_seriesFeedFragment_to_seriesDetailsFragment, bundle)
        }

        observeSeriesList()

        addSearchBar()

        observeSearchedSeries()




    }

    private fun setupRecyclerView() {

        seriesFeedAdapter = SeriesFeedAdapter(seriesList)
        seriesRecyclerView = binding.seriesFeedRecycler

        seriesRecyclerView.adapter = seriesFeedAdapter
        seriesRecyclerView.layoutManager = LinearLayoutManager(this.context)

        seriesRecyclerView.addOnScrollListener(this.scrollListener)
    }

    private fun observeSeriesList() {

        viewModel.seriesList.observe(viewLifecycleOwner, Observer { response ->

            when(response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        seriesFeedAdapter.addSeriesList(response.data.results)
                        isLastPage = viewModel.seriesPageNumber == response.data.total_pages
                        if(isLastPage) {
                            seriesRecyclerView.setPadding(0,0,0,0)
                        }
                    }

                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(this.context,"An error occurred! $it", Toast.LENGTH_LONG).show()
                    }

                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun observeSearchedSeries() {

        viewModel.searchedSeries.observe(viewLifecycleOwner, Observer { response ->

            when(response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        seriesFeedAdapter.addSeriesList(response.data.results)
                        isLastPage = viewModel.searchedSeriesPageNumber == response.data.total_pages
                        if(isLastPage) {
                            seriesRecyclerView.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(this.context,"An error occurred! $it", Toast.LENGTH_LONG).show()
                    }

                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }

        })
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

            val layoutManager = seriesRecyclerView.layoutManager as LinearLayoutManager
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
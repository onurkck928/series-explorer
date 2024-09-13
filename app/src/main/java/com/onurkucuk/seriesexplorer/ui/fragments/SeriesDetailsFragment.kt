package com.onurkucuk.seriesexplorer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.ui.MainActivity
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel


class SeriesDetailsFragment : Fragment() {

    lateinit var viewModel: SeriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }



}
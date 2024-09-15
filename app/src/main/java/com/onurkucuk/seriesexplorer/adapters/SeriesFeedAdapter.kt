package com.onurkucuk.seriesexplorer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onurkucuk.seriesexplorer.R
import com.onurkucuk.seriesexplorer.databinding.SeriesFeedRecyclerRowBinding
import com.onurkucuk.seriesexplorer.models.Series
import com.onurkucuk.seriesexplorer.ui.viewmodels.SeriesViewModel
import com.onurkucuk.seriesexplorer.util.Constants.Companion.BASE_IMAGE_URL
import com.squareup.picasso.Picasso


class SeriesFeedAdapter(private val seriesList: MutableList<Series>) : RecyclerView.Adapter<SeriesFeedAdapter.SeriesFeedViewHolder>() {

    inner class SeriesFeedViewHolder(private val itemBinding: SeriesFeedRecyclerRowBinding) : RecyclerView.ViewHolder(itemBinding.root) {


        fun onBind(series: Series) {


            itemBinding.seriesNameText.text = series.name
            Picasso.get().load("${BASE_IMAGE_URL}${series.backdrop_path}").into(itemBinding.seriesImage)

            // Set the favourite icon filled if the series is saved, set it unfilled otherwise
            if(SeriesViewModel.favouriteSeriesIdSet.contains(series.id)) {
                itemBinding.favouriteIcon.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                itemBinding.favouriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
            }

            itemBinding.favouriteIcon.setOnClickListener {
                if(SeriesViewModel.favouriteSeriesIdSet.contains(series.id)) {
                    SeriesViewModel.favouriteSeriesIdSet.remove(series.id)
                    itemBinding.favouriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
                } else {
                    SeriesViewModel.favouriteSeriesIdSet.add(series.id)
                    itemBinding.favouriteIcon.setImageResource(R.drawable.baseline_favorite_24)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesFeedViewHolder {
        val itemBinding = SeriesFeedRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesFeedViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    override fun onBindViewHolder(holder: SeriesFeedViewHolder, position: Int) {
        val series: Series = seriesList[position]

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(series)
            }
        }
        holder.onBind(series)
    }

    private var onItemClickListener: ((Series) -> Unit)? = null

    fun setOnItemClickListener(listener: (Series) -> Unit) {
        onItemClickListener = listener
    }
    fun addSeriesList(list :List<Series>?) {
        list?.let {
            seriesList.clear()
            seriesList.addAll(list)
            notifyDataSetChanged()
        }
    }
    fun reset() {
        seriesList.clear()
        notifyDataSetChanged()
    }
}
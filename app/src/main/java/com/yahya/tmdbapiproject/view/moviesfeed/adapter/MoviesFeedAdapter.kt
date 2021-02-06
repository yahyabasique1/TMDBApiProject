package com.yahya.tmdbapiproject.view.moviesfeed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yahya.tmdbapiproject.R
import com.yahya.tmdbapiproject.extensions.loadUrl
import com.yahya.tmdbapiproject.repository.network.MoviesData
import com.yahya.tmdbapiproject.view.moviesfeed.MoviesFeedFragmentDirections
import kotlinx.android.synthetic.main.item_movie_card.view.*

class MoviesFeedAdapter : PagingDataAdapter<MoviesData, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<MoviesData>() {
            override fun areItemsTheSame(oldItem: MoviesData, newItem: MoviesData) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MoviesData, newItem: MoviesData) =
                    oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? MoviesListViewHolder)?.bind(item = getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MoviesListViewHolder.getInstance(parent)

    }

    class MoviesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            //get instance of the NewsListImageViewHolder
            fun getInstance(parent: ViewGroup): MoviesListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_movie_card, parent, false)
                return MoviesListViewHolder(view)
            }
        }


        fun bind(item: MoviesData?, position: Int) {
            itemView.apply {
                item?.backdropPath.let {
                    ivMovieBanner.loadUrl("https://image.tmdb.org/t/p/w342/${it}")
                }
                tvDateAndSource.text = "${item?.vote_average} | ${item?.releaseDate ?: ""}"
                textView.text = item?.originalTitle ?: ""


                setOnClickListener {
                    if (item?.overview?.isNotEmpty() ?: false) {

                        val dir = MoviesFeedFragmentDirections.actionMovieListFragmentToMovieDetailFragment(item!!)
                        it.findNavController().navigate(dir)

                    }
                }

            }

        }
    }
}
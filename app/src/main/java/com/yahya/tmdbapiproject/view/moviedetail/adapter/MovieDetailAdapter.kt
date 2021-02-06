package com.yahya.tmdbapiproject.view.moviedetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yahya.tmdbapiproject.R
import com.yahya.tmdbapiproject.extensions.loadUrl
import kotlinx.android.synthetic.main.card_images.view.*
import kotlinx.android.synthetic.main.item_movie_card.view.*

class MovieDetailAdapter(val items : List<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return if(items.size>10) 10 else items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_images, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let {
            holder?.ivPosterImage.loadUrl("https://image.tmdb.org/t/p/w185/${it}")
        }
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val ivPosterImage = view.ivPosterImages
}
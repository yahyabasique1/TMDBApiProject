package com.yahya.tmdbapiproject.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.yahya.tmdbapiproject.R

fun ImageView.loadUrl(imageUrl: String){
    Glide.with(context)
        .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .into(this)
}
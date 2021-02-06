package com.yahya.tmdbapiproject.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(@PrimaryKey val movieId: Long, val prevKey: Int?, val nextKey: Int?)

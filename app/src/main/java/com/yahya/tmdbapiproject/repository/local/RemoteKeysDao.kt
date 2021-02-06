package com.yahya.tmdbapiproject.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remotekey WHERE movieId = :id")
    suspend fun remoteKeysMoviesInfo(id: Long): RemoteKey?

    @Query("DELETE FROM remotekey")
    suspend fun clearRemoteKeys()
}


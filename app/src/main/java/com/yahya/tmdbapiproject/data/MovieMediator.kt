package com.yahya.tmdbapiproject.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yahya.tmdbapiproject.data.MoviesRepository.Companion.DEFAULT_PAGE_INDEX
import com.yahya.tmdbapiproject.repository.local.AppDatabase
import com.yahya.tmdbapiproject.repository.local.RemoteKey
import com.yahya.tmdbapiproject.repository.network.MoviesData
import com.yahya.tmdbapiproject.repository.network.ResponseResult
import com.yahya.tmdbapiproject.repository.network.ResponseResult.Status
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class MovieMediator(val remoteDataSource: MoviesRemoteDataSource, val appDatabase: AppDatabase) : RemoteMediator<Int, MoviesData>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, MoviesData>): MediatorResult {
        val pagedKeydata = getKeyPageData(loadType, state)
//        Log.e("PAGEDDATA","$pagedKeydata")

//        val page = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                if (remoteKeys == null) {
//                    // The LoadType is PREPEND so some data was loaded before,
//                    // so we should have been able to get remote keys
//                    // If the remoteKeys are null, then we're an invalid state and we have a bug
//                    throw InvalidObjectException("Remote key and the prevKey should not be null")
//                }
//                // If the previous key is null, then we can't request more data
//                val prevKey = remoteKeys.prevKey
//                if (prevKey == null) {
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//                remoteKeys.prevKey
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                if (remoteKeys == null || remoteKeys.nextKey == null) {
//                    throw InvalidObjectException("Remote key should not be null for $loadType")
//                }
//                remoteKeys.nextKey
//            }
//
//        }
        val page=when(pagedKeydata){
            is MediatorResult.Success ->
                return pagedKeydata

            else ->
                pagedKeydata as Int
        }
//        Log.e("PAGEDDATA", "$pagedKeydata $page")

        try {
//            if (page == 3) {
//                page++
//            }

            val response = remoteDataSource.fetchMoviesList(page, "")
            val isEndofList = response.data?.moviesData?.isEmpty()
//            Log.e("RESP", "$response status ${response.status}")


            when (response.status) {
                ResponseResult.Status.SUCCESS -> {
                    appDatabase.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            appDatabase.getRepoDao().clearRemoteKeys()
                            appDatabase.getMoviesResultDao().clearAllMovies()
                        }

                        val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                        val nextKey = if(isEndofList!!.not())  page + 1 else null
//
                        val keys = response.data?.moviesData?.map {
                            RemoteKey(movieId = it.id!!, prevKey = prevKey, nextKey = nextKey)

                        }
                        Log.e("PAGEDDATA", "$pagedKeydata $isEndofList $prevKey $nextKey")

                        appDatabase.getRepoDao().insertAll(keys!!)
                        appDatabase.getMoviesResultDao().insertAll(response.data.moviesData)
                    }
                    return MediatorResult.Success(endOfPaginationReached = isEndofList ?: false)

                }

                Status.ERROR -> {
                    Log.e("PAGEDDATA", "$pagedKeydata $isEndofList error")

                    return MediatorResult.Error(Throwable(response.message))

                }
            }
            return MediatorResult.Error(Throwable(response.message))


        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    /**
     * this returns the page key or the final end of list success result
     */
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, MoviesData>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                        ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }


//    /**
//     * this returns the page key or the final end of list success result
//     */
//    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, MoviesData>): Any? {
//        return when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getClosestRemoteKey(state)
//                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getLastRemoteKey(state)
//                        ?: throw InvalidObjectException("Remote key should not be null for $loadType")
//                remoteKeys.nextKey
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getFirstRemoteKey(state)
//                        ?: throw InvalidObjectException("Invalid state, key should not be null")
//                //end of list condition reached
//                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
//                remoteKeys.prevKey
//            }
//        }
//    }


    private suspend fun getLastRemoteKey(state: PagingState<Int, MoviesData>): RemoteKey? {
        return state.pages
                .lastOrNull { it.data.isNotEmpty() }
                ?.data?.lastOrNull()
                ?.let { movieFeed ->
                    Log.e("Append","last  $state ${movieFeed.id}")
                    appDatabase.getRepoDao().remoteKeysMoviesInfo(movieFeed.id!!) }
    }


    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, MoviesData>): RemoteKey? {
        return state.pages
                .firstOrNull() { it.data.isNotEmpty() }
                ?.data?.firstOrNull()
                ?.let {
                    appDatabase.getRepoDao().remoteKeysMoviesInfo(it.id!!)
                }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, MoviesData>): RemoteKey? {
        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.id?.let { id ->
                Log.e("Append","closest $state. $id")

                appDatabase.getRepoDao().remoteKeysMoviesInfo(id)

            }
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MoviesData>): RemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { repo ->
                    // Get the remote keys of the last item retrieved
                    appDatabase.getRepoDao().remoteKeysMoviesInfo(repo.id!!)
                }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MoviesData>): RemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { repo ->
                    // Get the remote keys of the first items retrieved
                    appDatabase.getRepoDao().remoteKeysMoviesInfo(repo.id!!)
                }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
            state: PagingState<Int, MoviesData>
    ): RemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDatabase.getRepoDao().remoteKeysMoviesInfo(repoId)
            }
        }
    }
}
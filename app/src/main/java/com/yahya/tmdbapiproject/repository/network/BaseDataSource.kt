package com.yahya.tmdbapiproject.repository.network

import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call:suspend () ->Response<T>):ResponseResult<T>{
        try {
            val response=call()
            if(response.isSuccessful){
                val body=response.body()
                    return ResponseResult.success(response.body()!!)

            }
            return error(" ${response.code()} ${response.message()}")

        }catch (e: Exception) {
                return error(e.message ?: e.toString())
            }
        }

        private fun <T> error(message: String): ResponseResult<T> {

            /** We can deserialize error model (in case we get error msg from server)
             * and pass the message */
            Timber.e(message)
            return ResponseResult.error("Network call has failed for a following reason: $message")
        }
}
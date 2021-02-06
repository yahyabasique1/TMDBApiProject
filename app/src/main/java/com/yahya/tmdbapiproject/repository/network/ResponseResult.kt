package com.yahya.tmdbapiproject.repository.network

data class ResponseResult<out T>(val status:Status?, val data:T?, val message:String?){
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T): ResponseResult<T> {
            return ResponseResult(Status.SUCCESS, data, null)
        }
        fun <T> error(message: String, data: T? = null): ResponseResult<T> {
            return ResponseResult(Status.ERROR, data, message)
        }
        fun <T> loading(data: T? = null): ResponseResult<T> {
            return ResponseResult(Status.LOADING, data, null)
        }
    }
}

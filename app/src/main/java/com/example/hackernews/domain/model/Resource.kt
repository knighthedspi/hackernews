package com.example.hackernews.domain.model

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val errorCode: Int?,
    val message: String?
) {
    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(errorCode: Int?, message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, errorCode, message)
        }
    }
}
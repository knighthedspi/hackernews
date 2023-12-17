package com.example.hackernews.domain.datasource

import com.example.hackernews.domain.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {

    companion object {
        const val ERROR_OTHER = -99
    }

    protected suspend fun <T> getResult(
        dispatcher: CoroutineDispatcher,
        call: suspend () -> Response<T>
    ): Resource<T> {
        return withContext(dispatcher) {
            try {
                val response = call()
                val errorMessage = "Failed to get response body"
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        Resource.success(it)
                    } ?: error(response.code(), errorMessage)
                } else {
                    error(response.code(), response.message())
                }
            } catch (e: Exception) {
                Timber.e(e)
                error(ERROR_OTHER, e.message ?: e.toString())
            }
        }
    }

    private fun <T> error(errorCode: Int, message: String): Resource<T> {
        return Resource.error(errorCode, message)
    }

}
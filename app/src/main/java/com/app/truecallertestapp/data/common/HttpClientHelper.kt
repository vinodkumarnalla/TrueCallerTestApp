package com.app.truecallertestapp.data.common

import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class HttpClientHelper @Inject constructor(
    private val client: OkHttpClient
) {
    suspend fun get(url: String): Resource<String> {
        return try {
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (!it.isSuccessful) {
                    return Resource.Error("HTTP error ${it.code}: ${it.message}")
                }

                val body = it.body?.string()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Empty response body")
                }
            }
        } catch (e: Exception) {
            val message = getReadableErrorMessage(e)
            Resource.Error(message, e)
        }
    }

    private fun getReadableErrorMessage(exception: Throwable): String {
        return when (exception) {
            is java.net.UnknownHostException -> "No internet connection."
            is java.net.SocketTimeoutException -> "Connection timed out."
            is java.net.ConnectException -> "Couldn't connect to the server."
            is javax.net.ssl.SSLException -> "SSL error: ${exception.localizedMessage}"
            is java.io.IOException -> "Network error: ${exception.localizedMessage ?: "Unknown IO error"}"
            else -> "Unexpected error: ${exception.localizedMessage ?: "Unknown error"}"
        }
    }

}

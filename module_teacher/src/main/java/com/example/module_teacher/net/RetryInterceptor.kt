package com.example.module_teacher.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0

        while (tryCount < maxRetries && (response == null || !response.isSuccessful)) {
            try {
                response = chain.proceed(request)
            } catch (e: IOException) {
                exception = e
            } finally {
                tryCount++
            }
        }

        // Throw the last caught exception if the request did not succeed after retries
        if (response == null && exception != null) {
            throw exception
        }

        return response ?: throw IOException("Unknown error occurred during request retry.")
    }
}

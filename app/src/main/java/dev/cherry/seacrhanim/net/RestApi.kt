package dev.cherry.seacrhanim.net

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ConnectivityManager
import retrofit2.Call

/**
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */

class RestApi(val appContext: Context) {

    /**
     * Performs a retrofit call
     *
     * @param T typed parameter of return object
     * @param call [Call] retrofit request object
     * @return parsed json object of [T]
     */
    fun <T> execute(call: Call<T>): T {
        // check internet connection availability
        if (!isNetworkAble()) throw IllegalStateException("No network available")
        try {
            // make request
            val response = call.execute()

            // check response code
            if (response.isSuccessful) {
                return response.body()
            } else {
                throw NetworkErrorException("Server error occurred")
            }
        } catch (e: Exception) {
            // log an exception and throw network error
            e.printStackTrace()
            throw IllegalStateException(e)
        }
    }

    /**
     * Asks system is there network connection
     *
     * @return true if connection available, false otherwise
     */
    private fun isNetworkAble(): Boolean {
        val connectivityManager = appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}

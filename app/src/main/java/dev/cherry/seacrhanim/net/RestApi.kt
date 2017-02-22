package dev.cherry.seacrhanim.net

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ConnectivityManager
import dev.cherry.seacrhanim.App
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

/**
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */

class RestApi(val appContext: Context) {

    @Inject
    lateinit var okHttpClient: OkHttpClient

    init {
        // inject dependencies
        App.graph.inject(this)
    }

    /**
     * Performs a network request
     *
     * @param request [Request] OkHttp request object
     * @return server response as [String]
     */
    fun makeRequest(request: Request): String {
        // check internet connection availability
        if (!isNetworkAble()) throw NetworkErrorException("No network available")
        try {
            // make request
            val response = okHttpClient.newCall(request).execute()

            // get string representation of response
            val responseString = response.body().string()

            // check response code
            if (response.code() == 200) {
                return responseString
            } else {
                throw NetworkErrorException()
            }
        } catch (e: Exception) {
            // log an exception and throw network error
            e.printStackTrace()
            throw NetworkErrorException(e)
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

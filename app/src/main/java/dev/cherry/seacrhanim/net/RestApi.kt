package dev.cherry.seacrhanim.net

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer

/**
 * @author DVLP_2
 * *
 * @since 13.02.2017.
 */

class RestApi(private val mContext: Context) {

    private val mHttpClient: OkHttpClient

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        mHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    fun makeRequest(request: Request): String {
        if (!isNetworkAble()) throw NetworkErrorException("No network available")
        try {
            val response = mHttpClient.newCall(request).execute()
            val responseString = response.body().string()
            if (response.code() == 200) {
                return responseString
            } else {
                throw NetworkErrorException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw NetworkErrorException(e)
        }
    }

    /**
     * Converts request body to string
     *
     * @param request [Request] object
     * @return corresponding string
     */
    fun bodyToString(request: Request): String {
        try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body().writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: Exception) {
            return "cannot parse request body"
        }
    }

    /**
     * Asks system is there network connection
     *
     * @return true if connection available, false otherwise
     */
    private fun isNetworkAble(): Boolean {
        val connectivityManager = mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}

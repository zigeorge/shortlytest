package test.geo.shortly.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build


object ShortlyUtil {

    private const val HTTP_PREFIX = "http://"
    private const val HTTPS_PREFIX = "https://"
    private const val MIN_PART_SIZE_OF_URL = 2

    fun isInvalidLink(link: String): Boolean {
        val checkLink = if (link.contains(HTTP_PREFIX)) {
            link.removePrefix(HTTP_PREFIX)
        } else if (link.contains(HTTPS_PREFIX)) {
            link.removePrefix(HTTPS_PREFIX)
        } else link
        val parts = checkLink.split(".")
        return parts.size < MIN_PART_SIZE_OF_URL
    }

    fun checkNetworkConnection(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
            networkRequest.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            return nInfo != null && nInfo.isAvailable && nInfo.isConnected
        }

    }

}
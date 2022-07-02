package test.geo.shortly.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

/*
* Represents a singleton util class with some helper functions
* */

object ShortlyUtil {

    private const val HTTP_PREFIX = "http://"
    private const val HTTPS_PREFIX = "https://"
    private const val MIN_PART_SIZE_OF_URL = 2


    /*
    * Returns true if a link is invalid and false if not
    * @param link: String is the given link to validate
    * The validation mechanism is built checking if the provided link has at least one [.] in it
    * */
    fun isInvalidLink(link: String): Boolean {
        val checkLink = if (link.contains(HTTP_PREFIX)) {
            link.removePrefix(HTTP_PREFIX)
        } else if (link.contains(HTTPS_PREFIX)) {
            link.removePrefix(HTTPS_PREFIX)
        } else if (link.contains(" "))
            link.trim()
        else link
        val parts = checkLink.split(".")
        return parts.size < MIN_PART_SIZE_OF_URL
    }

    /*
    * Check if network connectivity is available in a given context
    * used deprecated methods here for older version support
    * */
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